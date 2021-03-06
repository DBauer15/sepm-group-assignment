package at.ac.tuwien.sepm.assignment.groupphase.application.persistence.implementation;

import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.NoEntryFoundException;
import at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation.CloseUtil;
import at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation.JDBCConnectionManager;
import org.junit.Assert;
import org.junit.Test;

import at.ac.tuwien.sepm.assignment.groupphase.application.dto.DietPlan;
import at.ac.tuwien.sepm.assignment.groupphase.application.dto.Recipe;
import at.ac.tuwien.sepm.assignment.groupphase.application.dto.RecipeIngredient;
import at.ac.tuwien.sepm.assignment.groupphase.application.dto.RecipeTag;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.DietPlanPersistence;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.RecipePersistence;
import at.ac.tuwien.sepm.assignment.groupphase.application.util.BaseTest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import static org.hamcrest.core.Is.is;

public class DBDietPlanPersistenceTest extends BaseTest{
	private static final String SQL_CHECK_DIET_PLAN_UPDATE = "SELECT * FROM Diet_Plan WHERE ID = ?;";

	@Test
	public void testCreate_withDietPlan_successWithSetDietPlanId() throws PersistenceException {
		DietPlanPersistence dietPlanPersistence = new DBDietPlanPersistence();

		DietPlan dietPlan = new DietPlan("Jeden Tag Sorgenfrei", 3000d, 100d, 200d, 300.50);
		dietPlanPersistence.create(dietPlan);

		Assert.assertNotNull(dietPlan.getId());
		Assert.assertTrue(dietPlan.getId() > 0);
	}

	@Test
    public void testReadAll_default_successWithMatchingCount() throws PersistenceException, SQLException {
        DietPlanPersistence dietPlanPersistence = new DBDietPlanPersistence();

        List<DietPlan> dietPlans = dietPlanPersistence.readAll();

        PreparedStatement countDietPlans = JDBCConnectionManager.getConnection().prepareStatement("select count(*) from diet_plan");
        ResultSet resultSet = countDietPlans.executeQuery();

        if (resultSet.next() == false) {
            Assert.fail("Could not retrieve diet plan count");
        }

        int count = resultSet.getInt(1);
        Assert.assertEquals(dietPlans.size(), count);
    }

    @Test
    public void testReadActice_withActiveDietPlan_successWithMatchingDietPlanIds() throws PersistenceException, NoEntryFoundException {
        DietPlanPersistence dietPlanPersistence = new DBDietPlanPersistence();

        DietPlan dietPlan = new DietPlan(3, "Jeden Tag Sorgenfrei", 3000d, 100d, 200d, 300.50, null, null);
        dietPlanPersistence.switchTo(dietPlan);

        DietPlan activeDietPlan = dietPlanPersistence.readActive();
        System.out.println(activeDietPlan);
        Assert.assertEquals(dietPlan.getId(), activeDietPlan.getId());
    }

    @Test (expected = NoEntryFoundException.class)
    public void testReadActive_withoutActiveDietPlan_failureThrowsNoEntryFoundException() throws PersistenceException, NoEntryFoundException {
	    DietPlanPersistence dietPlanPersistence = new DBDietPlanPersistence();

        DietPlan dietPlan = new DietPlan(Integer.MIN_VALUE, "Jeden Tag Sorgenfrei", 3000d, 100d, 200d, 300.50, null, null);
        dietPlanPersistence.switchTo(dietPlan);

        DietPlan plan = dietPlanPersistence.readActive();
    }

	@Test
    public void testSwitchTo_withDietPlan_successWithSetDietPlanDtFrom() throws PersistenceException, SQLException {
	    DietPlanPersistence dietPlanPersistence = new DBDietPlanPersistence();

	    DietPlan dietPlan = new DietPlan(2, "Jeden Tag Sorgenfrei", 3000d, 100d, 200d, 300.50, null, null);

        Connection connection = JDBCConnectionManager.getConnection();

        PreparedStatement getActiveStatement = connection.prepareStatement("select id from DIET_PLAN where to_dt IS NULL AND from_dt=(select max(from_dt) from DIET_PLAN);");
        PreparedStatement getOldStatement = connection.prepareStatement("select id from DIET_PLAN where id=? and to_dt IS NOT NULL");

        //Get currently active plan and save its ID for later
        ResultSet resultSetOldActive = getActiveStatement.executeQuery();
        int old_active_id = -1;
        if (resultSetOldActive.next() == true) {
            old_active_id = resultSetOldActive.getInt(1);
        }

        //Switch to the new plan
        dietPlanPersistence.switchTo(dietPlan);

        //Get the new plan and check if everything was changed correctly
        ResultSet resultSetNewActive = getActiveStatement.executeQuery();
        if (resultSetNewActive.next() == false) {
            Assert.fail("No diet plan was not set active");
        }
        Assert.assertThat(resultSetNewActive.getInt(1), is(dietPlan.getId()));
        getActiveStatement.close();

        //Check if the old plan was updated correctly
        if (old_active_id != dietPlan.getId() && old_active_id != -1) {
            getOldStatement.setInt(1, old_active_id);
            ResultSet resultSetOldInactive = getOldStatement.executeQuery();
            if (resultSetOldInactive.next() == false) {
                Assert.fail("No diet plan was not set inactive");
            }
            Assert.assertThat(resultSetOldInactive.getInt(1), is(old_active_id));
        }
        getOldStatement.close();

        JDBCConnectionManager.closeConnection();
	}	

    @Test
    public void testUpdateDietPlan_dietPlanIsValid_successWithDietPlanValuesUpdated() throws PersistenceException, SQLException {
        DietPlanPersistence dietPlanPersistence = new DBDietPlanPersistence();

        DietPlan toUpdate = new DietPlan("My custom plan", 4000d, 20d, 30d, 50d);
        dietPlanPersistence.create(toUpdate);
        dietPlanPersistence.update(toUpdate);

        PreparedStatement checkDietPlanUpdate = JDBCConnectionManager.getConnection().prepareStatement(SQL_CHECK_DIET_PLAN_UPDATE);
        checkDietPlanUpdate.setInt(1, toUpdate.getId());
        ResultSet rs = checkDietPlanUpdate.executeQuery();
        
        if (!rs.next()) {
            Assert.fail("Diet plan update failed because no diet plan with given id exists.");
        }

        Assert.assertEquals((int) toUpdate.getId(), rs.getInt("ID"));
        Assert.assertEquals(toUpdate.getName(), rs.getString("NAME"));
        Assert.assertEquals(toUpdate.getEnergy_kcal(), Double.valueOf(rs.getDouble("ENERG_KCAL")));
        Assert.assertEquals(toUpdate.getLipid(), Double.valueOf(rs.getDouble("LIPID")));
        Assert.assertEquals(toUpdate.getProtein(), Double.valueOf(rs.getDouble("PROTEIN")));
        Assert.assertEquals(toUpdate.getCarbohydrate(), Double.valueOf(rs.getDouble("CARBOHYDRT")));

        CloseUtil.closeStatement(checkDietPlanUpdate);
    }
}