package at.ac.tuwien.sepm.assignment.groupphase.application.persistence.implementation;

import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Rule;
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
import org.junit.rules.ExpectedException;

public class DBRecipePersistenceTest extends BaseTest {

	private static final String SQL_CHECK_RECIPE_CREATION = "select id, name,duration,description,tags,deleted from recipe where id=?;";
	private static final String SQL_CHECK_RECIPE_INGREDIENT_CREATION = "select ingredient_id, recipe_id, amount from recipe_ingredient where recipe_id=?;";
	private static final String SQL_CHECK_USER_SPECIFIC_INGREDIENT_CREATION = "select ID, NAME, ENERG_KCAL, LIPID, PROTEIN, CARBOHYDRT, UNIT_NAME, "
			+ "UNIT_GRAM_NORMALISED from ingredient where user_specific=true;";
	private static final String SQL_CHECK_RECIPE_UPDATE = "SELECT * FROM RECIPE WHERE ID = ?;";
    private static final String SQL_CHECK_RECIPE_INGREDIENT_UPDATE = "SELECT * FROM RECIPE_INGREDIENT r_i JOIN INGREDIENT i ON r_i.INGREDIENT_ID = i.ID WHERE r_i.RECIPE_ID = ?;";
    private static final String SQL_DROP_EXAMPLE_RECIPES = "DELETE FROM recipe_ingredient; DELETE FROM recipe;";

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

		Assert.assertNotNull(r.getId());
		Assert.assertTrue(r.getId() > 0);

		// verify recipe tuple
		PreparedStatement checkRecipeCreation = null;
		checkRecipeCreation = JDBCConnectionManager.getConnection().prepareStatement(SQL_CHECK_RECIPE_CREATION);
		checkRecipeCreation.setInt(1, r.getId());
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
		checkRecipeIngredientCreation.setInt(1, r.getId());
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

    @Test
    public void testGetRecipe_idIsValid_successWithRecipeSet() throws PersistenceException {
        RecipePersistence recipePersistence = new DBRecipePersistence();

        Recipe recipe = recipePersistence.get(1);

        Assert.assertEquals(1, (int) recipe.getId());
        Assert.assertEquals("Fresh salmon with Thai noodle salad", recipe.getName());
        Assert.assertEquals(20, recipe.getDuration(), 0.0);
        Assert.assertEquals("Put a pan of water on to boil. Line a steamer with baking parchment, add the salmon " +
            "fillets and scatter with a little of the orange zest. When the water is boiling, add the beans to the " +
            "pan, put the salmon in the steamer on top and cook for 5 mins. Take the salmon off, and if it is " +
            "cooked, set aside but add the peas and mange tout to the pan and cook for 1 min more, or if not quite " +
            "cooked leave on top for the extra min. Drain the veg, but return the boiling water to the pan, add the " +
            "noodles and leave to soak for 5 mins.&#10;Put the curry paste and fish sauce in a salad bowl with the " +
            "orange juice and a little of the remaining zest and the spring onions. Drain the noodles when they are " +
            "ready and add to the salad bowl, toss well, then add the chopped orange with the basil or coriander and " +
            "the cooked vegetables. Tip in the juice from the fish, then toss well and serve in bowls with the " +
            "salmon on top.", recipe.getDescription());
        Assert.assertEquals(EnumSet.of(RecipeTag.D, RecipeTag.L), recipe.getTags());
        Assert.assertEquals("DL", recipe.getTagsAsString());
        Assert.assertTrue(!recipe.getDeleted());
        Assert.assertEquals(9, recipe.getRecipeIngredients().size());
    }

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void testGetRecipe_idIsInvalid_throwsPersistenceException() throws PersistenceException {
        expectedEx.expect(PersistenceException.class);
        expectedEx.expectMessage("No recipe found for given id");

        new DBRecipePersistence().get(-1);
    }
    
    @Test
    public void testDeleteRecipe_idIsInvalid_throwsPersistenceException() throws PersistenceException {
        expectedEx.expect(PersistenceException.class);
        expectedEx.expectMessage("No recipe found for given id");
        
    	new DBRecipePersistence().delete(-1);
    }
    
    @Test
    public void testDeleteRecipe_idIsValid_success() throws PersistenceException, SQLException {
    	RecipePersistence recipePersistence = new DBRecipePersistence();
    	
    	recipePersistence.delete(1);
    	
        PreparedStatement checkRecipeUpdate = JDBCConnectionManager.getConnection().prepareStatement(SQL_CHECK_RECIPE_UPDATE);
        checkRecipeUpdate.setInt(1, 1);
        ResultSet rs = checkRecipeUpdate.executeQuery();
        if (!rs.next()) {
            Assert.fail("Recipe update failed because no recipe with given id exists.");
        }

        Assert.assertEquals((int) 1, rs.getInt("ID"));
        Assert.assertTrue(rs.getBoolean("DELETED"));
    }

    @Test
    public void testUpdateRecipe_recipeIsValidWithUserSpecificIngredients_successWithRecipeValuesUpdated() throws PersistenceException, SQLException {
        RecipePersistence recipePersistence = new DBRecipePersistence();

        Recipe toUpdate = new Recipe(1,
            "Updated recipe",
            33.3,
            "This recipe has been updated.",
            EnumSet.of(RecipeTag.B),
            false);

        List<RecipeIngredient> ingredients = new ArrayList<>();
        ingredients.add(new RecipeIngredient(1d, 1d, 1d, 1d, 1d, "A", 1d, true, "A"));
        ingredients.add(new RecipeIngredient(2d, 2d, 2d, 2d, 2d, "B", 2d, true, "B"));
        toUpdate.setRecipeIngredients(ingredients);

        recipePersistence.update(toUpdate);

        PreparedStatement checkRecipeUpdate = JDBCConnectionManager.getConnection().prepareStatement(SQL_CHECK_RECIPE_UPDATE);
        checkRecipeUpdate.setInt(1, 1);
        ResultSet rs = checkRecipeUpdate.executeQuery();
        if (!rs.next()) {
            Assert.fail("Recipe update failed because no recipe with given id exists.");
        }

        Assert.assertEquals((int) toUpdate.getId(), rs.getInt("ID"));
        Assert.assertEquals(toUpdate.getName(), rs.getString("NAME"));
        Assert.assertEquals(toUpdate.getDuration(), rs.getDouble("DURATION"), 0.0);
        Assert.assertEquals(toUpdate.getDescription(), rs.getString("DESCRIPTION"));
        Assert.assertEquals(toUpdate.getTagsAsString(), rs.getString("TAGS"));
        Assert.assertEquals(toUpdate.getDeleted(), rs.getBoolean("DELETED"));

        CloseUtil.closeStatement(checkRecipeUpdate);

        PreparedStatement checkIngredientsUpdate = JDBCConnectionManager.getConnection().prepareStatement(SQL_CHECK_RECIPE_INGREDIENT_UPDATE);
        checkIngredientsUpdate.setInt(1, 1);
        rs = checkIngredientsUpdate.executeQuery();

        int countIngredients = 0;
        while (rs.next()) {
            RecipeIngredient ingredient = toUpdate.getRecipeIngredients().get(countIngredients++);

            Assert.assertEquals(ingredient.getAmount(), rs.getDouble("AMOUNT"), 0.0);
            Assert.assertEquals(ingredient.getEnergyKcal(), rs.getDouble("ENERG_KCAL"), 0.0);
            Assert.assertEquals(ingredient.getLipid(), rs.getDouble("LIPID"), 0.0);
            Assert.assertEquals(ingredient.getProtein(), rs.getDouble("PROTEIN"), 0.0);
            Assert.assertEquals(ingredient.getCarbohydrate(), rs.getDouble("CARBOHYDRT"), 0.0);
            Assert.assertEquals(ingredient.getUnitName(), rs.getString("UNIT_NAME"));
            Assert.assertEquals(ingredient.getUnitGramNormalised(), rs.getDouble("UNIT_GRAM_NORMALISED"), 0.0);
            Assert.assertEquals(ingredient.getUserSpecific(), rs.getBoolean("USER_SPECIFIC"));
            Assert.assertEquals(ingredient.getIngredientName(), rs.getString("NAME"));
        }

        Assert.assertEquals(toUpdate.getRecipeIngredients().size(), countIngredients);
        CloseUtil.closeStatement(checkIngredientsUpdate);
    }

    @Test
    public void testUpdateRecipe_recipeIsValidWithCommonIngredients_successWithRecipeValuesUpdated() throws PersistenceException, SQLException {
        RecipePersistence recipePersistence = new DBRecipePersistence();

        Recipe toUpdate = new Recipe(1,
            "Updated recipe",
            33.3,
            "This recipe has been updated.",
            EnumSet.of(RecipeTag.B),
            false);

        List<RecipeIngredient> ingredients = new ArrayList<>();
        ingredients.add(new RecipeIngredient(1, 1d, false));
        ingredients.add(new RecipeIngredient(2, 2d, false));
        toUpdate.setRecipeIngredients(ingredients);

        recipePersistence.update(toUpdate);

        PreparedStatement checkRecipeUpdate = JDBCConnectionManager.getConnection().prepareStatement(SQL_CHECK_RECIPE_UPDATE);
        checkRecipeUpdate.setInt(1, 1);
        ResultSet rs = checkRecipeUpdate.executeQuery();
        if (!rs.next()) {
            Assert.fail("Recipe update failed because no recipe with given id exists.");
        }

        Assert.assertEquals((int) toUpdate.getId(), rs.getInt("ID"));
        Assert.assertEquals(toUpdate.getName(), rs.getString("NAME"));
        Assert.assertEquals(toUpdate.getDuration(), rs.getDouble("DURATION"), 0.0);
        Assert.assertEquals(toUpdate.getDescription(), rs.getString("DESCRIPTION"));
        Assert.assertEquals(toUpdate.getTagsAsString(), rs.getString("TAGS"));
        Assert.assertEquals(toUpdate.getDeleted(), rs.getBoolean("DELETED"));

        CloseUtil.closeStatement(checkRecipeUpdate);

        PreparedStatement checkIngredientsUpdate = JDBCConnectionManager.getConnection().prepareStatement(SQL_CHECK_RECIPE_INGREDIENT_UPDATE);
        checkIngredientsUpdate.setInt(1, 1);
        rs = checkIngredientsUpdate.executeQuery();

        int countIngredients = 0;
        while (rs.next()) {
            RecipeIngredient ingredient = toUpdate.getRecipeIngredients().get(countIngredients++);

            Assert.assertEquals((int) ingredient.getId(), rs.getInt("ID"));
            Assert.assertEquals(ingredient.getAmount(), rs.getDouble("AMOUNT"), 0.0);
            Assert.assertEquals(ingredient.getUserSpecific(), rs.getBoolean("USER_SPECIFIC"));
        }

        Assert.assertEquals(toUpdate.getRecipeIngredients().size(), countIngredients);
        CloseUtil.closeStatement(checkIngredientsUpdate);
    }

    @Test
    public void testgetRecipes_databaseHasRecipeEntries_successWithEntriesReturned() throws PersistenceException {
        RecipePersistence recipePersistence = new DBRecipePersistence();
        List<Recipe> recipes = recipePersistence.getRecipes();
        Assert.assertEquals(20, recipes.size());

        for (Recipe r : recipes) {
            Assert.assertNotNull(r.getId());
            Assert.assertNotNull(r.getName());
            Assert.assertNotNull(r.getDuration());
            Assert.assertNotNull(r.getDescription());
            Assert.assertNotNull(r.getTags());
            Assert.assertNotNull(r.getDeleted());

            for (RecipeIngredient i : r.getRecipeIngredients()) {
                Assert.assertNotNull(i.getId());
                Assert.assertNotNull(i.getAmount());
                Assert.assertNotNull(i.getUserSpecific());
                Assert.assertNotNull(i.getAmount());
                Assert.assertNotNull(i.getEnergyKcal());
                Assert.assertNotNull(i.getLipid());
                Assert.assertNotNull(i.getProtein());
                Assert.assertNotNull(i.getCarbohydrate());
                Assert.assertNotNull(i.getUnitName());
                Assert.assertNotNull(i.getUnitGramNormalised());
                Assert.assertNotNull(i.getUserSpecific());
                Assert.assertNotNull(i.getIngredientName());
            }
        }
    }

    @Test
    public void testgetRecipes_databaseIsEmpty_successWithListEmpty() throws PersistenceException, SQLException {
        PreparedStatement dropExampleRecipes = JDBCConnectionManager.getConnection().prepareStatement(SQL_DROP_EXAMPLE_RECIPES);
        dropExampleRecipes.execute();
        CloseUtil.closeStatement(dropExampleRecipes);

        RecipePersistence recipePersistence = new DBRecipePersistence();
        List<Recipe> recipes = recipePersistence.getRecipes();
        Assert.assertNotNull(recipes);
        Assert.assertEquals(0, recipes.size());
    }
}