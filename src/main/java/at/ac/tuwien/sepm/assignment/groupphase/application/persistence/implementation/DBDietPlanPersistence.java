package at.ac.tuwien.sepm.assignment.groupphase.application.persistence.implementation;

import java.lang.invoke.MethodHandles;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import at.ac.tuwien.sepm.assignment.groupphase.application.dto.DietPlan;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.DietPlanPersistence;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation.JDBCConnectionManager;

@Repository
public class DBDietPlanPersistence implements DietPlanPersistence {

	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private static final String SQL_CREATE_DIET_PLAN = "INSERT INTO diet_plan (name, energ_kcal, lipid, protein, carbohydrt) VALUES (?,?,?,?,?);";
	private static final String SQL_DEACTIVATE_DIET_PLAN = "UPDATE diet_plan SET to_dt=NOW() WHERE from_dt=(SELECT max(from_dt) FROM diet_plan WHERE from_dt IS NOT NULL);";
	private static final String SQL_ACTIVATE_DIET_PLAN = "UPDATE diet_plan SET from_dt=NOW(), to_dt=NULL WHERE id=?;";

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

	private void closePreparedStmnt(PreparedStatement stmnt) {
		try {
			if (stmnt != null && stmnt.isClosed() == false) {
				stmnt.close();
			}
		} catch (SQLException e) {
			LOG.error("Closing used Prepared Statement instance failed. {}", e);
		}
	}

}
