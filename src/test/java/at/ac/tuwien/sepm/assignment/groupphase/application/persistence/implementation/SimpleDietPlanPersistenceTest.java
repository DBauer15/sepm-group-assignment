package at.ac.tuwien.sepm.assignment.groupphase.application.persistence.implementation;

import org.junit.Assert;
import org.junit.Test;

import at.ac.tuwien.sepm.assignment.groupphase.application.dto.DietPlan;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.DietPlanPersistence;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.application.util.BaseTest;

public class SimpleDietPlanPersistenceTest extends BaseTest{

	@Test
	public void testCreate_withDietPlan_successWithSetDietPlanId() throws PersistenceException {
		DietPlanPersistence dietPlanPersistence = new SimpleDietPlanPersistence();

		DietPlan dietPlan = new DietPlan("Jeden Tag Sorgenfrei", 3000d, 100d, 200d, 300.50);
		dietPlanPersistence.create(dietPlan);

		Assert.assertNotNull(dietPlan.getId());
		Assert.assertTrue(dietPlan.getId() > 0);
	}

}
