package at.ac.tuwien.sepm.assignment.groupphase.application.persistence.implementation;

import java.lang.invoke.MethodHandles;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.NoEntryFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import at.ac.tuwien.sepm.assignment.groupphase.application.dto.DietPlan;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.DietPlanPersistence;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation.CloseUtil;
import at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation.JDBCConnectionManager;

@Repository
public class DBDietPlanPersistence implements DietPlanPersistence {
	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private static final String SQL_CREATE_DIET_PLAN = "INSERT INTO diet_plan (name, energ_kcal, lipid, protein, carbohydrt) VALUES (?,?,?,?,?);";
	private static final String SQL_READ_ALL = "SELECT * FROM diet_plan ORDER BY id;";
	private static final String SQL_READ_ACTIVE = "SELECT * FROM diet_plan WHERE to_dt IS NULL AND from_dt=(SELECT max(from_dt) FROM diet_plan);";
	private static final String SQL_DEACTIVATE_DIET_PLAN = "UPDATE diet_plan SET to_dt=NOW() WHERE from_dt=(SELECT max(from_dt) FROM diet_plan WHERE from_dt IS NOT NULL);";
	private static final String SQL_ACTIVATE_DIET_PLAN = "UPDATE diet_plan SET from_dt=NOW(), to_dt=NULL WHERE id=?;";
	private static final String SQL_UPDATE_CUSTOM_DIET_PLAN = "UPDATE diet_plan SET name = ?, energ_kcal = ?, lipid = ?, protein = ?, carbohydrt = ? WHERE id = ? AND id > 3;";
	
	@Override
	public void create(DietPlan dietPlan) throws PersistenceException {

		PreparedStatement createDietPlanStmnt = null;
		try {
			createDietPlanStmnt = JDBCConnectionManager.getConnection().prepareStatement(SQL_CREATE_DIET_PLAN,
					Statement.RETURN_GENERATED_KEYS);

			createDietPlanStmnt.setString(1, dietPlan.getName());
			createDietPlanStmnt.setDouble(2, dietPlan.getEnergy_kcal());
			createDietPlanStmnt.setDouble(3, dietPlan.getLipid());
			createDietPlanStmnt.setDouble(4, dietPlan.getProtein());
			createDietPlanStmnt.setDouble(5, dietPlan.getCarbohydrate());

			createDietPlanStmnt.execute();
			ResultSet generatedKeys = createDietPlanStmnt.getGeneratedKeys();
			generatedKeys.next();
			dietPlan.setId(generatedKeys.getInt(1));

		} catch (SQLException e) {
			throw new PersistenceException(
					"There was an error while creating the diet plan in the database. " + e.getMessage());
		} finally {
			closePreparedStmnt(createDietPlanStmnt);
		}
	}

	@Override
    public List<DietPlan> readAll() throws PersistenceException {
        LOG.debug("Reading all diet plans.");

        PreparedStatement readAllDietPlans = null;

        try {
            readAllDietPlans = JDBCConnectionManager.getConnection().prepareStatement(SQL_READ_ALL);
            ResultSet resultSet = readAllDietPlans.executeQuery();

            List<DietPlan> dietPlans = new ArrayList<>();

            while (resultSet.next() == true) {
                DietPlan dietPlan = readOneFrom(resultSet);
                dietPlans.add(dietPlan);
            }

            LOG.debug("Successfully read all diet plans.");
            return dietPlans;
        } catch (SQLException e) {
            throw new PersistenceException("There was an error while reading all diet plans. " + e.getMessage());
        } finally {
            closePreparedStmnt(readAllDietPlans);
        }
    }

    @Override
    public DietPlan readActive() throws PersistenceException, NoEntryFoundException {
        LOG.debug("Reading active diet plan.");

        PreparedStatement readActiveDietPlan = null;

        try {
            readActiveDietPlan = JDBCConnectionManager.getConnection().prepareStatement(SQL_READ_ACTIVE);
            ResultSet resultSet = readActiveDietPlan.executeQuery();

            if (resultSet.next() == false) {
                throw new NoEntryFoundException("No active diet plan set");
            }
            return readOneFrom(resultSet);

        } catch (SQLException e) {
            throw new PersistenceException("There was an error while reading the current diet plan. " + e.getMessage());
        } finally {
            closePreparedStmnt(readActiveDietPlan);
        }
    }

	@Override
    public void switchTo(DietPlan dietPlan) throws PersistenceException {
        LOG.debug("Switching to new diet plan. {}", dietPlan);

	    PreparedStatement deactivateDietPlan = null;
	    PreparedStatement activateDietPlan = null;

	    JDBCConnectionManager.startTransaction();

	    try {
	        deactivateDietPlan = JDBCConnectionManager.getConnection().prepareStatement(SQL_DEACTIVATE_DIET_PLAN);
	        deactivateDietPlan.executeUpdate();

	        activateDietPlan = JDBCConnectionManager.getConnection().prepareStatement(SQL_ACTIVATE_DIET_PLAN);
	        activateDietPlan.setInt(1, dietPlan.getId());
	        activateDietPlan.executeUpdate();

	        JDBCConnectionManager.commitTransaction();
	        LOG.debug("Successfully switched to new diet plan. {}", dietPlan);
        } catch (SQLException e) {
	        JDBCConnectionManager.rollbackTransaction();
            throw new PersistenceException("There was an error while switching the current diet plan. " + e.getMessage());
        } finally {
	        JDBCConnectionManager.finalizeTransaction();
            closePreparedStmnt(deactivateDietPlan);
            closePreparedStmnt(activateDietPlan);
        }
    }

    private DietPlan readOneFrom(ResultSet resultSet) throws SQLException {
        Integer id = resultSet.getInt("ID");
        String name = resultSet.getString("NAME");
        Double energKcal = resultSet.getDouble("ENERG_KCAL");
        Double lipid = resultSet.getDouble("LIPID");
        Double protein = resultSet.getDouble("PROTEIN");
        Double carbohydrt = resultSet.getDouble("CARBOHYDRT");
        Timestamp fromDtTimestamp = resultSet.getTimestamp("FROM_DT");
        LocalDate fromDt = fromDtTimestamp == null ? null : fromDtTimestamp.toLocalDateTime().toLocalDate();
        Timestamp toDtTimestamp = resultSet.getTimestamp("TO_DT");
        LocalDate toDt = toDtTimestamp == null ? null : toDtTimestamp.toLocalDateTime().toLocalDate();

        return new DietPlan(id, name, energKcal, lipid, protein, carbohydrt, fromDt, toDt);
	}

	private void closePreparedStmnt(PreparedStatement stmnt) {
		try {
			if (stmnt != null && stmnt.isClosed() == false) {
				stmnt.close();
			}
		} catch (SQLException e) {
			LOG.error("Closing used Prepared Statement instance failed. {}", e);
		}
	}

	@Override
	public void update(DietPlan dietPlan) throws PersistenceException {
		JDBCConnectionManager.startTransaction();
		PreparedStatement ps = null;
		
		try {
			ps = JDBCConnectionManager.getConnection().prepareStatement(SQL_UPDATE_CUSTOM_DIET_PLAN);

			ps.setString(1, dietPlan.getName());
			ps.setDouble(2, dietPlan.getEnergy_kcal());
			ps.setDouble(3, dietPlan.getLipid());
			ps.setDouble(4, dietPlan.getProtein());
			ps.setDouble(5, dietPlan.getCarbohydrate());
			ps.setInt(6, dietPlan.getId());
			
			if (ps.executeUpdate() == 0) {
				throw new PersistenceException("No diet plan found");			
			}

			JDBCConnectionManager.commitTransaction();
		} catch (SQLException e) {
			JDBCConnectionManager.rollbackTransaction();
			throw new PersistenceException(e);
		} finally {
			JDBCConnectionManager.finalizeTransaction();
			CloseUtil.closeStatement(ps);
		}
	}
}