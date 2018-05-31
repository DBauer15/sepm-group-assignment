package at.ac.tuwien.sepm.assignment.groupphase.application.service.implementation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.NoEntryFoundException;
import org.junit.Assert;
import org.junit.Test;

import at.ac.tuwien.sepm.assignment.groupphase.application.dto.DietPlan;
import at.ac.tuwien.sepm.assignment.groupphase.application.dto.Recipe;
import at.ac.tuwien.sepm.assignment.groupphase.application.dto.RecipeIngredient;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.DietPlanPersistence;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.implementation.DBDietPlanPersistence;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.DietPlanService;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.RecipeService;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.ServiceInvokationException;
import at.ac.tuwien.sepm.assignment.groupphase.application.util.BaseTest;

import static org.mockito.Mockito.*;

/**
 * Test class for {@link SimpleDietPlanService}
 * @author e01529136
 * @author e01442385
 */
public class SimpleDietPlanServiceTest extends BaseTest {

	// mocking
	private final DietPlanPersistence mockedDietPlanRepo = mock(DBDietPlanPersistence.class);

	// example data
	private static final String EXAMPLE_TEXT_256CHARS = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimatad";

	private DietPlan dietPlanValidWithId = new DietPlan(1, "Jeden Tag Sorgenfrei", 10d, 15d, 54.50d, 30.50d, null, null);
	private DietPlan dietPlanValidWithoutId = new DietPlan(null, "Jeden Tag Sorgenfrei", 10d, 15d, 54.50d, 30.50d, null, null);
	private DietPlan dietPlanValid = new DietPlan("Jeden Tag Sorgenfrei", 10d, 15d, 54.50d, 30.50d);
	private DietPlan dietPlanInvalid1 = new DietPlan(EXAMPLE_TEXT_256CHARS, 50000d, 6000d, 8000d, 9999.99);
	private DietPlan dietPlanInvalid2 = new DietPlan("Abnehmen", 0d, -1d, 0d, -2.33);
	private DietPlan dietPlanInvalid3 = new DietPlan(15, "My custom diet plan", 0d, -1d, -1d, -2.33, null, null);
	private DietPlan dietPlanForUpdate = new DietPlan(4, "My custom diet plan", 10d, 15d, 54.50d, 30.50d, null, null);


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
			Assert.assertEquals(6, errors.size());
			Assert.assertEquals("Enter only 255 characters in the field 'Diet plan name'", errors.get(0));
            Assert.assertEquals("Enter a value that is smaller than or equal to 10000.0 in the field 'Energy Kcal'", errors.get(1));
            Assert.assertEquals("Enter a value that is smaller than or equal to 100.0 in the field 'Fats'", errors.get(2));
            Assert.assertEquals("Enter a value that is smaller than or equal to 100.0 in the field 'Proteins'", errors.get(3));
            Assert.assertEquals("Enter a value that is smaller than or equal to 100.0 in the field 'Carbohydrates'", errors.get(4));
            Assert.assertEquals("The sum of 'Carbohydrates', 'Proteins' and 'Fats' has to be equal to 100%", errors.get(5));
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
			Assert.assertEquals(3, errors.size());
			Assert.assertEquals("Enter a value that is greater than or equal to 0.0 in the field 'Fats'", errors.get(0));
			Assert.assertEquals("Enter a value that is greater than or equal to 0.0 in the field 'Carbohydrates'", errors.get(1));
			Assert.assertEquals("The sum of 'Carbohydrates', 'Proteins' and 'Fats' has to be equal to 100%", errors.get(2));
			return;
		}
		Assert.fail("Should throw ServiceInvokationException!");
	}

	@Test
    public void testReadAll_none_callsPersistenceReadAllOnce() throws ServiceInvokationException, PersistenceException {
        // invocation
        DietPlanService dietPlanService = new SimpleDietPlanService(mockedDietPlanRepo);
        dietPlanService.readAll();

        // verification after invocation
        verify(mockedDietPlanRepo, times(1)).readAll();
    }

    @Test
    public void testReadAll_none_throwsServiceInvocationException() throws PersistenceException, NoEntryFoundException {
        when(mockedDietPlanRepo.readAll()).thenReturn(new ArrayList<DietPlan>(Arrays.asList(dietPlanInvalid1)));
        DietPlanService dietPlanService = new SimpleDietPlanService(mockedDietPlanRepo);

        try {
            dietPlanService.readAll();
        } catch (ServiceInvokationException e) {

            //Verify that persistence was called
            verify(mockedDietPlanRepo, times(1)).readAll();

            // verify validations
            ArrayList<String> errors = e.getContext().getErrors();
            Assert.assertEquals(7, errors.size());
            Assert.assertEquals("ID needs to be set", errors.get(0));
            Assert.assertEquals("Enter only 255 characters in the field 'Diet plan name'", errors.get(1));
            Assert.assertEquals("Enter a value that is smaller than or equal to 10000.0 in the field 'Energy Kcal'", errors.get(2));
            Assert.assertEquals("Enter a value that is smaller than or equal to 100.0 in the field 'Fats'", errors.get(3));
            Assert.assertEquals("Enter a value that is smaller than or equal to 100.0 in the field 'Proteins'", errors.get(4));
            Assert.assertEquals("Enter a value that is smaller than or equal to 100.0 in the field 'Carbohydrates'", errors.get(5));
            Assert.assertEquals("The sum of 'Carbohydrates', 'Proteins' and 'Fats' has to be equal to 100%", errors.get(6));
            
            return;
        }
        Assert.fail("Should throw NoEntryFoundException!");
    }

    @Test
    public void testReadActive_withActiveDietPlan_callsPersistenceReadActiveOnce() throws NoEntryFoundException, PersistenceException, ServiceInvokationException {
        // invocation
        when(mockedDietPlanRepo.readActive()).thenReturn(dietPlanValidWithId);
        DietPlanService dietPlanService = new SimpleDietPlanService(mockedDietPlanRepo);
        dietPlanService.readActive();

        // verification after invocation
        verify(mockedDietPlanRepo, times(1)).readActive();
    }

    @Test
    public void testReadActive_withoutActiveDietPlan_throwsNoEntryException() throws PersistenceException, ServiceInvokationException, NoEntryFoundException {
	    when(mockedDietPlanRepo.readActive()).thenThrow(NoEntryFoundException.class);
        DietPlanService dietPlanService = new SimpleDietPlanService(mockedDietPlanRepo);

        try {
            dietPlanService.readActive();
        } catch (NoEntryFoundException e) {
            //Verify that persistence was called
            verify(mockedDietPlanRepo, times(1)).readActive();
            return;
        }
        Assert.fail("Should throw NoEntryFoundException!");
    }

    @Test
    public void testReadActive_withInvalidActiveDietPlan_throwsServiceInvocationException() throws PersistenceException, ServiceInvokationException, NoEntryFoundException {
        when(mockedDietPlanRepo.readActive()).thenReturn(dietPlanInvalid1);
        DietPlanService dietPlanService = new SimpleDietPlanService(mockedDietPlanRepo);

        try {
            dietPlanService.readActive();
        } catch (ServiceInvokationException e) {

            //Verify that persistence was called
            verify(mockedDietPlanRepo, times(1)).readActive();

            // verify validations
            ArrayList<String> errors = e.getContext().getErrors();
            Assert.assertEquals(7, errors.size());
            Assert.assertEquals("ID needs to be set", errors.get(0));
            Assert.assertEquals("Enter only 255 characters in the field 'Diet plan name'", errors.get(1));
            Assert.assertEquals("Enter a value that is smaller than or equal to 10000.0 in the field 'Energy Kcal'", errors.get(2));
            Assert.assertEquals("Enter a value that is smaller than or equal to 100.0 in the field 'Fats'", errors.get(3));
            Assert.assertEquals("Enter a value that is smaller than or equal to 100.0 in the field 'Proteins'", errors.get(4));
            Assert.assertEquals("Enter a value that is smaller than or equal to 100.0 in the field 'Carbohydrates'", errors.get(5));
            Assert.assertEquals("The sum of 'Carbohydrates', 'Proteins' and 'Fats' has to be equal to 100%", errors.get(6));
            return;
        }
        Assert.fail("Should throw NoEntryFoundException!");
    }


	@Test
    public void testSwitchTo_validData_callsPersistenceSwitchToOnce() throws ServiceInvokationException, PersistenceException {
        // invocation
        DietPlanService dietPlanService = new SimpleDietPlanService(mockedDietPlanRepo);
        dietPlanService.switchTo(dietPlanValidWithId);

        // verification after invokation
        verify(mockedDietPlanRepo, times(1)).switchTo(dietPlanValidWithId);
    }

    @Test
    public void testSwitchTo_invalidDataHasNoId_notCallsPersistenceSwitchToAndValidation() {
        // invocation
        DietPlanService dietPlanService = new SimpleDietPlanService(mockedDietPlanRepo);
        try {
            dietPlanService.switchTo(dietPlanValidWithoutId);
        } catch (ServiceInvokationException e) {

            // verification - no interaction with repo
            verifyZeroInteractions(mockedDietPlanRepo);

            // verify validations
            ArrayList<String> errors = e.getContext().getErrors();
            Assert.assertEquals(1, errors.size());
            Assert.assertEquals("ID needs to be set", errors.get(0));
            return;
        }
        Assert.fail("Should throw ServiceInvokationException!");
    }
    

    @Test
    public void testUpdate_dietPlanIsValid_callsPersistenceUpdateOnce() throws ServiceInvokationException, PersistenceException {
        DietPlanService dietPlanService = new SimpleDietPlanService(mockedDietPlanRepo);
        dietPlanService.update(dietPlanForUpdate);
        
        verify(mockedDietPlanRepo, times(1)).update(dietPlanForUpdate);
    }

    @Test
    public void testUpdate_dietPlanHasNoId_throwsValidationError() {
    	DietPlanService dietPlanService = new SimpleDietPlanService(mockedDietPlanRepo);
        DietPlan dietPlanNotValidForUpdate = dietPlanValidWithoutId;

        try {
        	dietPlanService.update(dietPlanNotValidForUpdate);
        } catch (ServiceInvokationException e) {
            verifyZeroInteractions(mockedDietPlanRepo);

            ArrayList<String> errors = e.getContext().getErrors();
            Assert.assertEquals(1, errors.size());
            Assert.assertEquals("ID needs to be set", errors.get(0));
            return;
        }
        
        Assert.fail("Should throw ServiceInvokationException!");
    }

    @Test
    public void testUpdate_dietPlanIsInvalid_throwsValidationError() {
    	DietPlanService dietPlanService = new SimpleDietPlanService(mockedDietPlanRepo);
        DietPlan recipeNotValidForUpdate = dietPlanInvalid3;

        try {
        	dietPlanService.update(recipeNotValidForUpdate);
        } catch (ServiceInvokationException e) {
            verifyZeroInteractions(mockedDietPlanRepo);

            ArrayList<String> errors = e.getContext().getErrors();
            Assert.assertEquals(4, errors.size());
            Assert.assertEquals("Enter a value that is greater than or equal to 0.0 in the field 'Fats'", errors.get(0));
            Assert.assertEquals("Enter a value that is greater than or equal to 0.0 in the field 'Proteins'", errors.get(1));
            Assert.assertEquals("Enter a value that is greater than or equal to 0.0 in the field 'Carbohydrates'", errors.get(2));
            Assert.assertEquals("The sum of 'Carbohydrates', 'Proteins' and 'Fats' has to be equal to 100%", errors.get(3));
            return;
        }
        
        Assert.fail("Should throw ServiceInvokationException!");
    }
}