package at.ac.tuwien.sepm.assignment.groupphase.application.service.implementation;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import at.ac.tuwien.sepm.assignment.groupphase.application.dto.DietPlan;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.DietPlanPersistence;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.implementation.SimpleDietPlanPersistence;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.DietPlanService;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.ServiceInvokationException;
import at.ac.tuwien.sepm.assignment.groupphase.application.util.BaseTest;

/**
 * Test class for {@link SimpleDietPlanService}
 * @author e01529136
 */
public class SimpleDietPlanServiceTest extends BaseTest {

	// mocking
	private final DietPlanPersistence mockedDietPlanRepo = mock(SimpleDietPlanPersistence.class);

	// example data
	private static final String EXAMPLE_TEXT_256CHARS = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimatad";

	private DietPlan dietPlanValid = new DietPlan("Jeden Tag Sorgenfrei", 10d, 15d, 20d, 30.50);
	private DietPlan dietPlanInvalid1 = new DietPlan(EXAMPLE_TEXT_256CHARS, 5000d, 6000d, 8000d, 9999.99);
	private DietPlan dietPlanInvalid2 = new DietPlan("Abnehmen", 0d, -1d, 0d, -2.33);

	@Test
	public void testCreate_validData_callsPersistenceCreateOnce()
			throws PersistenceException, ServiceInvokationException {
		// invokation
		DietPlanService dietPlanService = new SimpleDietPlanService(mockedDietPlanRepo);
		dietPlanService.create(dietPlanValid);

		// verification after invokation
		verify(mockedDietPlanRepo, times(1)).create(dietPlanValid);
	}

	@Test
	public void testCreate_invalidDataExceedsUpperLimits_notCallsPersistenceCreateAndValidations()
			throws PersistenceException {
		// invokation
		DietPlanService dietPlanService = new SimpleDietPlanService(mockedDietPlanRepo);
		try {
			dietPlanService.create(dietPlanInvalid1);
		} catch (ServiceInvokationException e) {

			// verification - no interaction with repo
			verifyZeroInteractions(mockedDietPlanRepo);

			// verify validations
			ArrayList<String> errors = e.getContext().getErrors();
			Assert.assertEquals(5, errors.size());
			Assert.assertEquals("Enter only 255 characters in the field 'Diet plan name'", errors.get(0));
			Assert.assertEquals("Enter a value that is smaller than 50.0 in the field 'Energy Kcal'", errors.get(1));
			Assert.assertEquals("Enter a value that is smaller than 50.0 in the field 'Lipid'", errors.get(2));
			Assert.assertEquals("Enter a value that is smaller than 50.0 in the field 'Protein'", errors.get(3));
			Assert.assertEquals("Enter a value that is smaller than 50.0 in the field 'Carbohydrate'", errors.get(4));
			return;
		}
		Assert.fail("Should throw ServiceInvokationException!");
	}

	@Test
	public void testCreate_invalidDataFallsBelowLimit_notCallsPersistenceCreateAndValidations()
			throws PersistenceException {
		// invokation
		DietPlanService dietPlanService = new SimpleDietPlanService(mockedDietPlanRepo);
		try {
			dietPlanService.create(dietPlanInvalid2);
		} catch (ServiceInvokationException e) {

			// verification - no interaction with repo
			verifyZeroInteractions(mockedDietPlanRepo);

			// verify validations
			ArrayList<String> errors = e.getContext().getErrors();
			Assert.assertEquals(4, errors.size());
			Assert.assertEquals("Enter a value that is greater than 0.0 in the field 'Energy Kcal'", errors.get(0));
			Assert.assertEquals("Enter a value that is greater than 0.0 in the field 'Lipid'", errors.get(1));
			Assert.assertEquals("Enter a value that is greater than 0.0 in the field 'Protein'", errors.get(2));
			Assert.assertEquals("Enter a value that is greater than 0.0 in the field 'Carbohydrate'", errors.get(3));
			return;
		}
		Assert.fail("Should throw ServiceInvokationException!");
	}

}
