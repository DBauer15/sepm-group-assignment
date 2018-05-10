package at.ac.tuwien.sepm.assignment.groupphase.application.persistence.implementation;

import at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation.JDBCConnectionManager;
import org.junit.Assert;
import org.junit.Test;

import at.ac.tuwien.sepm.assignment.groupphase.application.dto.DietPlan;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.DietPlanPersistence;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.application.util.BaseTest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.hamcrest.core.Is.is;

public class SimpleDietPlanPersistenceTest extends BaseTest{

	@Test
	public void testCreate_withDietPlan_successWithSetDietPlanId() throws PersistenceException {
		DietPlanPersistence dietPlanPersistence = new SimpleDietPlanPersistence();

		DietPlan dietPlan = new DietPlan("Jeden Tag Sorgenfrei", 3000d, 100d, 200d, 300.50);
		dietPlanPersistence.create(dietPlan);

		Assert.assertNotNull(dietPlan.getId());
		Assert.assertTrue(dietPlan.getId() > 0);
	}

	@Test
    public void testSwitchTo_withDietPlan_successWithSetDietPlanDtFrom() throws PersistenceException, SQLException {
	    DietPlanPersistence dietPlanPersistence = new SimpleDietPlanPersistence();

	    DietPlan dietPlan = new DietPlan(2, "Jeden Tag Sorgenfrei", 3000d, 100d, 200d, 300.50, null, null);

        Connection connection = JDBCConnectionManager.getConnection();

        PreparedStatement getActiveStatement = connection.prepareStatement("select id from DIET_PLAN where to_dt IS NULL AND from_dt=(select max(from_dt) from DIET_PLAN);");
        PreparedStatement getOldStatement = connection.prepareStatement("select id from DIET_PLAN where id=? and to_dt IS NOT NULL");

        //Get currently active plan and save its ID for later
        ResultSet resultSetOldActive = getActiveStatement.executeQuery();
        if (resultSetOldActive.next() == false) {
            Assert.fail("No diet plan was not set active");
        }
        int old_active_id = resultSetOldActive.getInt(1);

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
        if (old_active_id != dietPlan.getId()) {
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

}
