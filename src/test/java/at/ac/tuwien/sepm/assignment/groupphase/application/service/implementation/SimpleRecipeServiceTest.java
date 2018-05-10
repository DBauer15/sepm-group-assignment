package at.ac.tuwien.sepm.assignment.groupphase.application.service.implementation;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import at.ac.tuwien.sepm.assignment.groupphase.application.dto.Recipe;
import at.ac.tuwien.sepm.assignment.groupphase.application.dto.RecipeIngredient;
import at.ac.tuwien.sepm.assignment.groupphase.application.dto.RecipeTag;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.RecipePersistence;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.implementation.DBRecipePersistence;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.RecipeService;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.ServiceInvokationException;
import at.ac.tuwien.sepm.assignment.groupphase.application.util.BaseTest;

public class SimpleRecipeServiceTest extends BaseTest {

	// mocking
	private final RecipePersistence mockedRecipeRepo = mock(DBRecipePersistence.class);

	// example data
	private EnumSet<RecipeTag> validTagBreakdfastSet = EnumSet.noneOf(RecipeTag.class);

	private static final String EXAMPLE_TEXT_256CHARS = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimatad";

	private Recipe recipeValid = new Recipe("My recipe", 120d, "Test", validTagBreakdfastSet);
	private Recipe recipeInvalid1 = new Recipe(EXAMPLE_TEXT_256CHARS, 0d, "", EnumSet.noneOf(RecipeTag.class));

	public SimpleRecipeServiceTest() {
		validTagBreakdfastSet.add(RecipeTag.B);
	}

	@Test
	public void testCreate_validData_callsPersistenceCreateOnce()
			throws ServiceInvokationException, PersistenceException {

		// invokation
		RecipeService dietPlanService = new SimpleRecipeService(mockedRecipeRepo);
		dietPlanService.create(recipeValid);

		// verification after invokation
		verify(mockedRecipeRepo, times(1)).create(recipeValid);
	}

	@Test
	public void testCreate_invalidDataFallsBelowLimits_notCallsPersistenceCreateAndValidations()
			throws PersistenceException {
		// invokation
		RecipeService recipeService = new SimpleRecipeService(mockedRecipeRepo);
		try {
			recipeService.create(recipeInvalid1);
		} catch (ServiceInvokationException e) {

			// verification - no interaction with repo
			verifyZeroInteractions(mockedRecipeRepo);

			// verify validations
			ArrayList<String> errors = e.getContext().getErrors();
			Assert.assertEquals(4, errors.size());
			Assert.assertEquals("Enter only 255 characters in the field 'Recipe name'", errors.get(0));
			Assert.assertEquals("Enter a value that is greater than 0.0 in the field 'Duration'", errors.get(1));
			Assert.assertEquals("Enter at least 1 characters in the field 'Description'", errors.get(2));
			Assert.assertEquals("Select at least one tag (breakfast, lunch or dinner)", errors.get(3));
			return;
		}
		Assert.fail("Should throw ServiceInvokationException!");
	}

	@Test
	public void testSearchIngredient_validData_callsPersistenceOnceWithTrimmedQueryAndReturnsListFromPersistence()
			throws ServiceInvokationException, PersistenceException {

		// prepare mock
		List<RecipeIngredient> mockedResult = new ArrayList<>();
		mockedResult.add(new RecipeIngredient(3, null, 5.55, 6.66, 7.77, 8.88, "cup", 10.10, false, "Eggnog"));

		// mock
		when(mockedRecipeRepo.searchIngredient("eggnog")).thenReturn(mockedResult);

		// invokation
		RecipeService dietPlanService = new SimpleRecipeService(mockedRecipeRepo);
		List<RecipeIngredient> actualResult = dietPlanService.searchIngredient("     eggnog    ");

		// verification after invokation
		verify(mockedRecipeRepo, times(1)).searchIngredient("eggnog");

		Assert.assertEquals(mockedResult, actualResult);
	}
	
	
	@Test
	public void testSearchIngredient_validDataWherePersistenceReturnsEmptyList_callsPersistenceOnceAndReturnsEmptyListFromPersistence()
			throws ServiceInvokationException, PersistenceException {

		// prepare mock - empty list
		List<RecipeIngredient> mockedResult = new ArrayList<>();

		// mock
		when(mockedRecipeRepo.searchIngredient("jaja")).thenReturn(mockedResult);

		// invokation
		RecipeService dietPlanService = new SimpleRecipeService(mockedRecipeRepo);
		List<RecipeIngredient> actualResult = dietPlanService.searchIngredient("jaja");

		// verification after invokation
		verify(mockedRecipeRepo, times(1)).searchIngredient("jaja");

		Assert.assertEquals(mockedResult, actualResult);
	}

}
