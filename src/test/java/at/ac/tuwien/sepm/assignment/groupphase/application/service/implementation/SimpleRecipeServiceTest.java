package at.ac.tuwien.sepm.assignment.groupphase.application.service.implementation;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation.IngredientSearchParamValidator;
import at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation.RecipeIngredientsValidator;
import at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation.RecipeValidator;
import org.junit.Assert;
import org.junit.Test;

import at.ac.tuwien.sepm.assignment.groupphase.application.dto.IngredientSearchParam;
import at.ac.tuwien.sepm.assignment.groupphase.application.dto.Recipe;
import at.ac.tuwien.sepm.assignment.groupphase.application.dto.RecipeImage;
import at.ac.tuwien.sepm.assignment.groupphase.application.dto.RecipeIngredient;
import at.ac.tuwien.sepm.assignment.groupphase.application.dto.RecipeSearchParam;
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
	private EnumSet<RecipeTag> validTagBreakfastSet = EnumSet.noneOf(RecipeTag.class);

	private static final String EXAMPLE_TEXT_256CHARS = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimatad";

	private Recipe recipeValid = new Recipe("My recipe", 120d, "Test", validTagBreakfastSet);
	private Recipe recipeValid2 = new Recipe("My recipe", 120d, "Test", validTagBreakfastSet);
	private Recipe recipeInvalid1 = new Recipe(EXAMPLE_TEXT_256CHARS, 0d, " ", EnumSet.noneOf(RecipeTag.class));
	private Recipe recipeInvalid2 = new Recipe(" ", 2566d, "something to do", validTagBreakfastSet);
	private Recipe recipeInvalid3 = new Recipe("My recipe", 120d, "Test", validTagBreakfastSet);
	private Recipe recipeInvalid4 = new Recipe("My recipe", 120d, "Test", validTagBreakfastSet);

	public SimpleRecipeServiceTest() {
		validTagBreakfastSet.add(RecipeTag.B);

		// valid recipe ingredient list
		List<RecipeIngredient> recipeIngredientList = new ArrayList<>();
		RecipeIngredient ri1 = new RecipeIngredient(2d, 55.5, 40d, 55.5, 4.5, "oz", 120d, true, "Watermelon");
		RecipeIngredient ri2 = new RecipeIngredient(1.4, 210d, 40d, 26.9, 21d, "oz", 50d, true, "Cheese");
		recipeIngredientList.add(ri1);
		recipeIngredientList.add(ri2);
		RecipeIngredient ri3 = new RecipeIngredient(45, 3.5, false);
		RecipeIngredient ri4 = new RecipeIngredient(101, 2d, false);
		recipeIngredientList.add(ri3);
		recipeIngredientList.add(ri4);
		recipeValid.setRecipeIngredients(recipeIngredientList);
		recipeValid2.setRecipeIngredients(recipeIngredientList);

		// invalid recipe ingredient list
		List<RecipeIngredient> invalidRecipeIngredientList1 = new ArrayList<>();
		invalidRecipeIngredientList1.add(ri3);
		invalidRecipeIngredientList1.add(ri4);

		RecipeIngredient ri5 = new RecipeIngredient(0d, 0d, 100.9d, 101.1, 100.01, "oz", 0d, true, " ");
		RecipeIngredient ri6 = new RecipeIngredient(0.1, 0.1, 100d, 100d, 100d, "lt", 0.5, true, " Walnut ");
		RecipeIngredient ri7 = new RecipeIngredient(-1d, -1d, 80d, 80d, 80d, " ", 5d, true, " Ice cube");
		RecipeIngredient ri8 = new RecipeIngredient(0.8, 110d, 0.1d, 0d, 99.9, "oz", 5d, true, EXAMPLE_TEXT_256CHARS);
		RecipeIngredient ri9 = new RecipeIngredient(47, 0d, false);
		RecipeIngredient ri10 = new RecipeIngredient(46, -2d, false);
		invalidRecipeIngredientList1.add(ri5);
		invalidRecipeIngredientList1.add(ri6);
		invalidRecipeIngredientList1.add(ri7);
		invalidRecipeIngredientList1.add(ri8);
		invalidRecipeIngredientList1.add(ri9);
		invalidRecipeIngredientList1.add(ri10);

		// invalid recipe ingredient list 2
		List<RecipeIngredient> invalidRecipeIngredientList2 = new ArrayList<>();
		RecipeIngredient ri11 = new RecipeIngredient(45, 2d, false);
		RecipeIngredient ri12 = new RecipeIngredient(45, 2d, false);
		RecipeIngredient ri13 = new RecipeIngredient(45, 3d, false);
		invalidRecipeIngredientList2.add(ri11);
		invalidRecipeIngredientList2.add(ri12);
		invalidRecipeIngredientList2.add(ri13);

		// invalid recipe ingredient list
		recipeInvalid1.setRecipeIngredients(new ArrayList<>());
		recipeInvalid2.setRecipeIngredients(null);
		recipeInvalid3.setRecipeIngredients(invalidRecipeIngredientList1);
		recipeInvalid4.setRecipeIngredients(invalidRecipeIngredientList2);
	}

	@Test
	public void testCreate_validDataWithImage_callsPersistenceCreateOnce()
			throws ServiceInvokationException, PersistenceException {

		// invokation
		RecipeService dietPlanService = new SimpleRecipeService(mockedRecipeRepo, new RecipeValidator(new RecipeIngredientsValidator()), new IngredientSearchParamValidator());
		List<RecipeImage> recipeImages = new ArrayList<>();
		recipeImages.add(new RecipeImage(new BufferedImage(100, 100, BufferedImage.TYPE_BYTE_GRAY), "png"));
		recipeValid2.setRecipeImages(recipeImages);
		
		dietPlanService.create(recipeValid2);

		// verification after invokation
		verify(mockedRecipeRepo, times(1)).create(recipeValid2);
	}
	
	@Test
	public void testCreate_validData_callsPersistenceCreateOnce()
			throws ServiceInvokationException, PersistenceException {

		// invokation
		RecipeService dietPlanService = new SimpleRecipeService(mockedRecipeRepo, new RecipeValidator(new RecipeIngredientsValidator()), new IngredientSearchParamValidator());
		dietPlanService.create(recipeValid);

		// verification after invokation
		verify(mockedRecipeRepo, times(1)).create(recipeValid);
	}

	@Test
	public void testCreate_invalidDataFallsBelowLimitsAndEmptyIngredientList_notCallsPersistenceCreateAndValidations()
			throws PersistenceException {
		// invokation
		RecipeService recipeService = new SimpleRecipeService(mockedRecipeRepo, new RecipeValidator(new RecipeIngredientsValidator()), new IngredientSearchParamValidator());
		try {
			recipeService.create(recipeInvalid1);
		} catch (ServiceInvokationException e) {

			// verification - no interaction with repo
			verifyZeroInteractions(mockedRecipeRepo);

			// verify validations
			ArrayList<String> errors = e.getContext().getErrors();
			Assert.assertEquals(5, errors.size());
			Assert.assertEquals("Enter only 255 characters in the field 'Recipe name'", errors.get(0));
			Assert.assertEquals("Enter a value that is greater than 0.0 in the field 'Duration'", errors.get(1));
			Assert.assertEquals("Enter at least 1 characters in the field 'Description'", errors.get(2));
			Assert.assertEquals("Select at least one tag (breakfast, lunch or dinner)", errors.get(3));
			Assert.assertEquals("Select at least one ingredient for the recipe.", errors.get(4));
			return;
		}
		Assert.fail("Should throw ServiceInvokationException!");
	}

	@Test
	public void testCreate_invalidDataDurationExceedsLimitAndIngredientListIsNull_notCallsPersistenceCreateAndValidations()
			throws PersistenceException {
		// invokation
		RecipeService recipeService = new SimpleRecipeService(mockedRecipeRepo, new RecipeValidator(new RecipeIngredientsValidator()), new IngredientSearchParamValidator());
		try {
			recipeService.create(recipeInvalid2);
		} catch (ServiceInvokationException e) {

			// verification - no interaction with repo
			verifyZeroInteractions(mockedRecipeRepo);

			// verify validations
			ArrayList<String> errors = e.getContext().getErrors();
			Assert.assertEquals(3, errors.size());
			Assert.assertEquals("Enter at least 1 characters in the field 'Recipe name'", errors.get(0));
			Assert.assertEquals("Enter a value that is smaller than 1441.0 in the field 'Duration'", errors.get(1));
			Assert.assertEquals("The field 'Ingredient Selection' cannot be null", errors.get(2));
			return;
		}
		Assert.fail("Should throw ServiceInvokationException!");
	}

	@Test
	public void testCreate_invalidDataWithInvalidIngredientData_notCallsPersistenceCreateAndValidations()
			throws PersistenceException {
		// invokation
		RecipeService recipeService = new SimpleRecipeService(mockedRecipeRepo, new RecipeValidator(new RecipeIngredientsValidator()), new IngredientSearchParamValidator());
		try {
			recipeService.create(recipeInvalid3);
		} catch (ServiceInvokationException e) {

			// verification - no interaction with repo
			verifyZeroInteractions(mockedRecipeRepo);

			// verify validations
			ArrayList<String> errors = e.getContext().getErrors();
			Assert.assertEquals(15, errors.size());
			Assert.assertEquals("Enter a value that is greater than 0.0 in the field 'Amount of ingredient '",
					errors.get(0));
			Assert.assertEquals("Enter at least 1 characters in the field 'Ingredient Name'", errors.get(1));
			Assert.assertEquals("Enter a value that is greater than 0.0 in the field 'Unit gram of ingredient '",
					errors.get(2));
			Assert.assertEquals(
					"Enter a value that is smaller than or equal to 100.0 in the field 'Carbohydrate of ingredient '",
					errors.get(3));
			Assert.assertEquals(
					"Enter a value that is smaller than or equal to 100.0 in the field 'Lipid of ingredient '",
					errors.get(4));
			Assert.assertEquals(
					"Enter a value that is smaller than or equal to 100.0 in the field 'Protein of ingredient '",
					errors.get(5));
			Assert.assertEquals(
					"Enter a value that is smaller than or equal to 100.0 in the field 'Sum of nutrient/100g for ingredient '",
					errors.get(6));

			Assert.assertEquals(
					"Enter a value that is smaller than or equal to 100.0 in the field 'Sum of nutrient/100g for ingredient Walnut'",
					errors.get(7));

			Assert.assertEquals("Enter a value that is greater than 0.0 in the field 'Amount of ingredient Ice cube'",
					errors.get(8));
			Assert.assertEquals("Enter at least 1 characters in the field 'Unit name of ingredient Ice cube'",
					errors.get(9));
			Assert.assertEquals(
					"Enter a value that is greater than or equal to 0.0 in the field 'Energy (kcal) of ingredient Ice cube'",
					errors.get(10));
			Assert.assertEquals(
					"Enter a value that is smaller than or equal to 100.0 in the field 'Sum of nutrient/100g for ingredient Ice cube'",
					errors.get(11));
			Assert.assertEquals("Enter only 255 characters in the field 'Ingredient Name'",
					e.getContext().getErrors().get(12));
			Assert.assertEquals("Enter a value that is greater than 0.0 in the field 'Amount of ingredient '",
					errors.get(13));
			Assert.assertEquals("Enter a value that is greater than 0.0 in the field 'Amount of ingredient '",
					errors.get(14));
			return;
		}
		Assert.fail("Should throw ServiceInvokationException!");
	}

	@Test
	public void testCreate_invalidDataMultipleIngredientsWithSameIds_notCallsPersistenceCreateAndValidations()
			throws PersistenceException {
		// invokation
		RecipeService recipeService = new SimpleRecipeService(mockedRecipeRepo, new RecipeValidator(new RecipeIngredientsValidator()), new IngredientSearchParamValidator());
		try {
			recipeService.create(recipeInvalid4);
		} catch (ServiceInvokationException e) {

			// verification - no interaction with repo
			verifyZeroInteractions(mockedRecipeRepo);

			// verify validations
			ArrayList<String> errors = e.getContext().getErrors();
			Assert.assertEquals(1, errors.size());
			Assert.assertEquals("The same ingredient cannot be added more than once.", errors.get(0));
			return;
		}
		Assert.fail("Should throw ServiceInvokationException!");
	}

	@Test
	public void testSearchIngredient_validData_callsPersistenceOnceAndReturnsListFromPersistence()
			throws ServiceInvokationException, PersistenceException {
		IngredientSearchParam searchParam = new IngredientSearchParam("eggnog");

		// prepare mock
		List<RecipeIngredient> mockedResult = new ArrayList<>();
		mockedResult.add(new RecipeIngredient(3, null, 5.55, 6.66, 7.77, 8.88, "cup", 10.10, false, "Eggnog"));

		// mock
		when(mockedRecipeRepo.searchIngredient(searchParam)).thenReturn(mockedResult);

		// invokation
		RecipeService dietPlanService = new SimpleRecipeService(mockedRecipeRepo, new RecipeValidator(new RecipeIngredientsValidator()), new IngredientSearchParamValidator());
		List<RecipeIngredient> actualResult = dietPlanService.searchIngredient(searchParam);

		// verification after invokation
		verify(mockedRecipeRepo, times(1)).searchIngredient(searchParam);

		Assert.assertEquals(mockedResult, actualResult);
	}

	@Test
	public void testSearchIngredient_validDataWherePersistenceReturnsEmptyList_callsPersistenceOnceAndReturnsEmptyListFromPersistence()
			throws ServiceInvokationException, PersistenceException {

		IngredientSearchParam searchParam = new IngredientSearchParam("jaja");

		// prepare mock - empty list
		List<RecipeIngredient> mockedResult = new ArrayList<>();

		// mock
		when(mockedRecipeRepo.searchIngredient(searchParam)).thenReturn(mockedResult);

		// invokation
		RecipeService dietPlanService = new SimpleRecipeService(mockedRecipeRepo, new RecipeValidator(new RecipeIngredientsValidator()), new IngredientSearchParamValidator());
		List<RecipeIngredient> actualResult = dietPlanService.searchIngredient(searchParam);

		// verification after invokation
		verify(mockedRecipeRepo, times(1)).searchIngredient(searchParam);

		Assert.assertEquals(mockedResult, actualResult);
	}

	@Test
	public void testSearchIngredient_invalidDataWhereParamObjectIsNull_notCallsPersistenceAndValidation() {

		IngredientSearchParam searchParam = null;

		// invokation
		RecipeService dietPlanService = new SimpleRecipeService(mockedRecipeRepo, new RecipeValidator(new RecipeIngredientsValidator()), new IngredientSearchParamValidator());
		try {
			dietPlanService.searchIngredient(searchParam);
		} catch (ServiceInvokationException e) {
			// verification - no interaction with repo
			verifyZeroInteractions(mockedRecipeRepo);
			Assert.assertEquals(1, e.getContext().getErrors().size());
			Assert.assertEquals("The field 'Ingredient Search Param' cannot be null",
					e.getContext().getErrors().get(0));
			return;
		}
		Assert.fail("Should throw ServiceInvokationException.");
	}

	@Test
	public void testSearchIngredient_invalidDataWhereIngredientNameIsNull_notCallsPersistenceAndValidation() {

		IngredientSearchParam searchParam = new IngredientSearchParam(null);

		// invokation
		RecipeService dietPlanService = new SimpleRecipeService(mockedRecipeRepo, new RecipeValidator(new RecipeIngredientsValidator()), new IngredientSearchParamValidator());
		try {
			dietPlanService.searchIngredient(searchParam);
		} catch (ServiceInvokationException e) {
			// verification - no interaction with repo
			verifyZeroInteractions(mockedRecipeRepo);
			Assert.assertEquals(1, e.getContext().getErrors().size());
			Assert.assertEquals("The field 'Ingredient Name' cannot be null", e.getContext().getErrors().get(0));
			return;
		}
		Assert.fail("Should throw ServiceInvokationException.");
	}

	@Test
	public void testSearchIngredient_invalidDataWhereIngredientNameLengthIsLessThan3Chars_notCallsPersistenceAndValidation() {

		IngredientSearchParam searchParam = new IngredientSearchParam(" Eg "); // 2 chars + 2 spaces

		// invokation
		RecipeService dietPlanService = new SimpleRecipeService(mockedRecipeRepo, new RecipeValidator(new RecipeIngredientsValidator()), new IngredientSearchParamValidator());
		try {
			dietPlanService.searchIngredient(searchParam);
		} catch (ServiceInvokationException e) {
			// verification - no interaction with repo
			verifyZeroInteractions(mockedRecipeRepo);
			Assert.assertEquals(1, e.getContext().getErrors().size());
			Assert.assertEquals("Enter at least 3 characters in the field 'Ingredient Name'",
					e.getContext().getErrors().get(0));
			return;
		}
		Assert.fail("Should throw ServiceInvokationException.");
	}

	@Test
	public void testSearchIngredient_invalidDataWhereIngredientNameLengthIsGreaterThan20Chars_notCallsPersistenceAndValidation() {

		IngredientSearchParam searchParam = new IngredientSearchParam(" Lorem ipsum dolor sit "); // 21 chars + 2 spaces

		// invokation
		RecipeService dietPlanService = new SimpleRecipeService(mockedRecipeRepo, new RecipeValidator(new RecipeIngredientsValidator()), new IngredientSearchParamValidator());
		try {
			dietPlanService.searchIngredient(searchParam);
		} catch (ServiceInvokationException e) {
			// verification - no interaction with repo
			verifyZeroInteractions(mockedRecipeRepo);
			Assert.assertEquals(1, e.getContext().getErrors().size());
			Assert.assertEquals("Enter only 20 characters in the field 'Ingredient Name'",
					e.getContext().getErrors().get(0));
			return;
		}
		Assert.fail("Should throw ServiceInvokationException.");
	}

	@Test
	public void testUpdate_recipeIsValid_callsPersistenceUpdateOnce()
			throws ServiceInvokationException, PersistenceException {

		RecipeService dietPlanService = new SimpleRecipeService(mockedRecipeRepo, new RecipeValidator(new RecipeIngredientsValidator()), new IngredientSearchParamValidator());
		recipeValid.setId(1);
		dietPlanService.update(recipeValid);
		verify(mockedRecipeRepo, times(1)).update(recipeValid);
	}

	@Test
	public void testUpdate_recipeHasNoId_throwsValidationError() {
		RecipeService recipeService = new SimpleRecipeService(mockedRecipeRepo, new RecipeValidator(new RecipeIngredientsValidator()), new IngredientSearchParamValidator());
		Recipe recipeNotValidForUpdate = recipeValid;

		try {
			recipeService.update(recipeNotValidForUpdate);
		} catch (ServiceInvokationException e) {
			verifyZeroInteractions(mockedRecipeRepo);

			ArrayList<String> errors = e.getContext().getErrors();
			Assert.assertEquals(1, errors.size());
			Assert.assertEquals("ID needs to be set", errors.get(0));
			return;
		}
		Assert.fail("Should throw ServiceInvokationException!");
	}

	@Test
	public void testDelete_validData_callsPersistenceCreateOnce() throws ServiceInvokationException, PersistenceException {
		RecipeService recipeService = new SimpleRecipeService(mockedRecipeRepo, new RecipeValidator(new RecipeIngredientsValidator()), new IngredientSearchParamValidator());
		recipeService.delete(1);

		// verification after invokation
		verify(mockedRecipeRepo, times(1)).delete(1);
	}
	
	@Test
	public void testSearchRecipes_paramIsEmpty_callsPersistenceOnce()
			throws ServiceInvokationException, PersistenceException {

		RecipeService recipeService = new SimpleRecipeService(mockedRecipeRepo, new RecipeValidator(new RecipeIngredientsValidator()), new IngredientSearchParamValidator());
		RecipeSearchParam searchParam = new RecipeSearchParam();
		
		recipeService.searchRecipes(searchParam);
		verify(mockedRecipeRepo, times(1)).searchRecipes(searchParam);
	}
	
	@Test
	public void testSearchRecipes_paramHasValidSearchCriteria_callsPersistenceOnce()
			throws ServiceInvokationException, PersistenceException {

		RecipeService recipeService = new SimpleRecipeService(mockedRecipeRepo, new RecipeValidator(new RecipeIngredientsValidator()), new IngredientSearchParamValidator());
		
		RecipeSearchParam searchParam = new RecipeSearchParam();
		searchParam.addIngredient("milk");
		searchParam.addIngredient("cheese");
		searchParam.setLowerDurationInkl(10d);
		searchParam.setUpperDurationInkl(30d);
		searchParam.setRecipeName("cottage");
		EnumSet<RecipeTag> tags = EnumSet.noneOf(RecipeTag.class);
		tags.add(RecipeTag.B);
		tags.add(RecipeTag.L);
		searchParam.setTags(tags);
		
		recipeService.searchRecipes(searchParam);
		verify(mockedRecipeRepo, times(1)).searchRecipes(searchParam);
	}
	
	@Test
	public void testSearchRecipes_tooMuchIngredientsAndInvalidDurationOrder_notCallsPersistenceAndValidations()
			throws ServiceInvokationException, PersistenceException {

		RecipeService recipeService = new SimpleRecipeService(mockedRecipeRepo, new RecipeValidator(new RecipeIngredientsValidator()), new IngredientSearchParamValidator());
		
		RecipeSearchParam searchParam = new RecipeSearchParam();
		searchParam.addIngredient("milk");
		searchParam.addIngredient("cheese");
		searchParam.addIngredient("yogurt");
		searchParam.addIngredient("marmelade");
		searchParam.addIngredient("alcohol");
		searchParam.addIngredient("pudding");
		searchParam.addIngredient("pork");
		searchParam.addIngredient("meat");
		searchParam.addIngredient("cream");
		searchParam.addIngredient("apple");
		searchParam.addIngredient("pie"); // too much ingredients, exceeding upper limit
		searchParam.setLowerDurationInkl(30d);
		searchParam.setUpperDurationInkl(10d); // invalid order
		searchParam.setRecipeName("");
		EnumSet<RecipeTag> tags = EnumSet.noneOf(RecipeTag.class);
		searchParam.setTags(tags);
		
		try {
			recipeService.searchRecipes(searchParam);
		} catch (ServiceInvokationException e) {
			verifyZeroInteractions(mockedRecipeRepo);

			ArrayList<String> errors = e.getContext().getErrors();
			Assert.assertEquals(2, errors.size());
			Assert.assertEquals("Enter only at most 10 ingredient search words.", errors.get(0));
			Assert.assertEquals("Lower Duration Limit must be smaller or equal to Upper Duration Limit.", errors.get(1));
			return;
		}
		Assert.fail("Should throw ServiceInvokationException!");
	}
}
