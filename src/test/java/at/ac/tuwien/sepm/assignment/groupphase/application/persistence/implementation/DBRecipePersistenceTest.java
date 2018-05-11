package at.ac.tuwien.sepm.assignment.groupphase.application.persistence.implementation;

import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;

import at.ac.tuwien.sepm.assignment.groupphase.application.dto.IngredientSearchParam;
import at.ac.tuwien.sepm.assignment.groupphase.application.dto.Recipe;
import at.ac.tuwien.sepm.assignment.groupphase.application.dto.RecipeIngredient;
import at.ac.tuwien.sepm.assignment.groupphase.application.dto.RecipeTag;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.RecipePersistence;
import at.ac.tuwien.sepm.assignment.groupphase.application.util.BaseTest;
import at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation.CloseUtil;
import at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation.JDBCConnectionManager;

public class DBRecipePersistenceTest extends BaseTest {

	private static final String SQL_CHECK_RECIPE_CREATION = "select id, name,duration,description,tags,deleted from recipe;";
	private static final String SQL_CHECK_RECIPE_INGREDIENT_CREATION = "select ingredient_id, recipe_id, amount from recipe_ingredient;";
	private static final String SQL_CHECK_USER_SPECIFIC_INGREDIENT_CREATION = "select ID, NAME, ENERG_KCAL, LIPID, PROTEIN, CARBOHYDRT, UNIT_NAME, "
			+ "UNIT_GRAM_NORMALISED from ingredient where user_specific=true;";

	public DBRecipePersistenceTest() {
		// valid recipe ingredient list

	}

	private List<RecipeIngredient> getUserSpecificRecipeIngredients() {
		List<RecipeIngredient> recipeIngredientList = new ArrayList<>();
		RecipeIngredient ri1 = new RecipeIngredient(2d, 55.5, 66.6, 77.7, 88.8, "oz", 120d, true, "Watermelon");
		RecipeIngredient ri2 = new RecipeIngredient(1.4, 88.9, 78d, 56d, 100d, "oz", 50d, true, "Cheese");
		recipeIngredientList.add(ri1);
		recipeIngredientList.add(ri2);
		return recipeIngredientList;
	}

	private List<RecipeIngredient> getCommonRecipeIngredients() {
		List<RecipeIngredient> recipeIngredientList = new ArrayList<>();
		RecipeIngredient ri3 = new RecipeIngredient(45, 3.5, false);
		RecipeIngredient ri4 = new RecipeIngredient(101, 2d, false);
		recipeIngredientList.add(ri3);
		recipeIngredientList.add(ri4);
		return recipeIngredientList;
	}

	@Test
	public void testCreate_withRecipeAndMixOfUserAndCommonIngredients_successWithSetRecipeId()
			throws PersistenceException, SQLException {
		RecipePersistence recipePersistence = new DBRecipePersistence();

		EnumSet<RecipeTag> set = EnumSet.noneOf(RecipeTag.class);
		set.add(RecipeTag.B);

		Recipe recipe = new Recipe("My recipe", 120d, "Test", set);

		List<RecipeIngredient> recipeIngredientList = new ArrayList<>();
		recipeIngredientList.addAll(getUserSpecificRecipeIngredients());
		recipeIngredientList.addAll(getCommonRecipeIngredients());
		recipe.setRecipeIngredients(recipeIngredientList);

		recipePersistence.create(recipe);

		Assert.assertNotNull(recipe.getId());
		Assert.assertTrue(recipe.getId() > 0);

		verifyRecipeCreation(recipe);
	}

	@Test
	public void testCreate_withRecipeAndOnlyUserIngredients_successWithSetRecipeId()
			throws PersistenceException, SQLException {
		RecipePersistence recipePersistence = new DBRecipePersistence();

		EnumSet<RecipeTag> set = EnumSet.noneOf(RecipeTag.class);
		set.add(RecipeTag.B);

		Recipe recipe = new Recipe("My recipe", 120d, "Test", set);

		List<RecipeIngredient> recipeIngredientList = new ArrayList<>();
		recipeIngredientList.addAll(getUserSpecificRecipeIngredients());
		recipe.setRecipeIngredients(recipeIngredientList);

		recipePersistence.create(recipe);

		Assert.assertNotNull(recipe.getId());
		Assert.assertTrue(recipe.getId() > 0);

		verifyRecipeCreation(recipe);
	}

	private void verifyRecipeCreation(Recipe r) throws SQLException, PersistenceException {

		// verify recipe tuple
		PreparedStatement checkRecipeCreation = null;
		checkRecipeCreation = JDBCConnectionManager.getConnection().prepareStatement(SQL_CHECK_RECIPE_CREATION);
		ResultSet resultSet = checkRecipeCreation.executeQuery();
		if (resultSet.next() == false) {
			Assert.fail("Recipe creation failed because no tupel in Recipe Table exists.");
		}

		Integer recipeId = resultSet.getInt("ID");
		String name = resultSet.getString("NAME");
		Double duration = resultSet.getDouble("DURATION");
		Clob descriptionClob = resultSet.getClob("DESCRIPTION");
		String tags = resultSet.getString("TAGS");
		Boolean deletedFlag = resultSet.getBoolean("DELETED");

		Assert.assertEquals(r.getId(), recipeId);
		Assert.assertNotNull(name);
		Assert.assertEquals(r.getName(), name);
		Assert.assertEquals(0, Double.compare(duration, r.getDuration()));
		Assert.assertEquals("B", tags);
		Assert.assertEquals("Test", descriptionClob.getSubString(1, (int) descriptionClob.length()));
		Assert.assertFalse(deletedFlag);
		CloseUtil.closeStatement(checkRecipeCreation);

		// verify recipe ingredient size
		List<Integer> createdIngredientIds = new ArrayList<>();

		PreparedStatement checkRecipeIngredientCreation = null;
		checkRecipeIngredientCreation = JDBCConnectionManager.getConnection()
				.prepareStatement(SQL_CHECK_RECIPE_INGREDIENT_CREATION);
		resultSet = checkRecipeIngredientCreation.executeQuery();

		int countRecipeIngredient = 0;
		while (resultSet.next() == true) {
			++countRecipeIngredient;

			Integer recipeId2 = resultSet.getInt("RECIPE_ID");
			Integer ingredientId = resultSet.getInt("INGREDIENT_ID");
			Double amount = resultSet.getDouble("AMOUNT");
			Assert.assertEquals(r.getId(), recipeId2);
			createdIngredientIds.add(ingredientId);
		}
		Assert.assertEquals(r.getRecipeIngredients().size(), countRecipeIngredient);
		CloseUtil.closeStatement(checkRecipeIngredientCreation);

		// check user ingredients

		PreparedStatement checkUserSpecificIngredients = null;
		checkUserSpecificIngredients = JDBCConnectionManager.getConnection()
				.prepareStatement(SQL_CHECK_USER_SPECIFIC_INGREDIENT_CREATION);
		resultSet = checkUserSpecificIngredients.executeQuery();

		// private static final String SQL_CHECK_USER_SPECIFIC_INGREDIENT_CREATION =
		// "select NAME, ENERG_KCAL, LIPID, PROTEIN, CARBOHYDRT, UNIT_NAME, "
		// + "UNIT_GRAM_NORMALISED, USER_SPECIFIC from ingredient;";

		List<RecipeIngredient> userSpecificRecipeIngredients = r.getRecipeIngredients().stream()
				.filter(ri -> ri.getUserSpecific() == true).collect(Collectors.toList());

		int countUserIngredients = 0;
		while (resultSet.next() == true) {
			++countUserIngredients;

			Integer id = resultSet.getInt("ID");
			String ingredientName = resultSet.getString("NAME");
			Double energyKcal = resultSet.getDouble("ENERG_KCAL");
			Double lipid = resultSet.getDouble("LIPID");
			Double protein = resultSet.getDouble("PROTEIN");
			Double carbohydrate = resultSet.getDouble("CARBOHYDRT");
			String unitName = resultSet.getString("UNIT_NAME");
			Double unitGramNormalised = resultSet.getDouble("UNIT_GRAM_NORMALISED");
			Assert.assertTrue(createdIngredientIds.contains(id));
		}
		Assert.assertEquals(userSpecificRecipeIngredients.size(), countUserIngredients);
		CloseUtil.closeStatement(checkUserSpecificIngredients);
	}

	@Test
	public void testCreate_withRecipeAndOnlyCommonIngredients_successWithSetRecipeId()
			throws PersistenceException, SQLException {
		RecipePersistence recipePersistence = new DBRecipePersistence();

		EnumSet<RecipeTag> set = EnumSet.noneOf(RecipeTag.class);
		set.add(RecipeTag.B);

		Recipe recipe = new Recipe("My recipe", 120d, "Test", set);

		List<RecipeIngredient> recipeIngredientList = new ArrayList<>();
		recipeIngredientList.addAll(getCommonRecipeIngredients());
		recipe.setRecipeIngredients(recipeIngredientList);

		recipePersistence.create(recipe);

		Assert.assertNotNull(recipe.getId());
		Assert.assertTrue(recipe.getId() > 0);

		verifyRecipeCreation(recipe);
	}

	@Test
	public void testSearchIngredient_queryIsEgg_resultSetNotEmptyAndContainsSpecificIngredient()
			throws PersistenceException {
		RecipePersistence recipePersistence = new DBRecipePersistence();

		List<RecipeIngredient> result = recipePersistence.searchIngredient(new IngredientSearchParam("egg"));
		Assert.assertTrue(result.size() >= 37);

		boolean found = false;
		for (RecipeIngredient ri : result) {
			if ("Eggnog".equals(ri.getIngredientName()) == false) {
				continue;
			}
			Assert.assertTrue(56 == ri.getId());
			Assert.assertTrue(88.0 == ri.getEnergyKcal());
			Assert.assertTrue(4.19 == ri.getLipid());
			Assert.assertTrue(4.55 == ri.getProtein());
			Assert.assertTrue(8.05 == ri.getCarbohydrate());
			Assert.assertTrue(254.0 == ri.getUnitGramNormalised());
			Assert.assertFalse(ri.getUserSpecific());
			Assert.assertEquals("cup", ri.getUnitName());
			found = true;
		}
		if (found == false) {
			Assert.fail("At least one Recipe ingredient was not transformed "
					+ "correct from resultset or ingredient data is incomplete.");
		}
	}
}