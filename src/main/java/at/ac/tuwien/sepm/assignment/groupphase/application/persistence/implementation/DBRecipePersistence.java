
package at.ac.tuwien.sepm.assignment.groupphase.application.persistence.implementation;

import java.lang.invoke.MethodHandles;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import at.ac.tuwien.sepm.assignment.groupphase.application.dto.IngredientSearchParam;
import at.ac.tuwien.sepm.assignment.groupphase.application.dto.Recipe;
import at.ac.tuwien.sepm.assignment.groupphase.application.dto.RecipeIngredient;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.RecipePersistence;
import at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation.CloseUtil;
import at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation.JDBCConnectionManager;

@Repository
public class DBRecipePersistence implements RecipePersistence {
	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private static final String CREATE_RECIPE = "INSERT INTO RECIPE (name, duration, description, tags, deleted) VALUES (?, ?, ?, ?, ?)";
	private static final String CREATE_USER_INGREDIENT = "INSERT INTO INGREDIENT (NAME, ENERG_KCAL, LIPID, PROTEIN, CARBOHYDRT, UNIT_NAME, "
			+ "UNIT_GRAM_NORMALISED, USER_SPECIFIC) VALUES (?,?,?,?,?,?,?, true);";
	private static final String SEARCH_INGREDIENT = "SELECT ID, NAME, ENERG_KCAL, LIPID, PROTEIN, CARBOHYDRT, "
			+ "UNIT_NAME, UNIT_GRAM_NORMALISED, USER_SPECIFIC FROM ingredient WHERE name ILIKE ? ORDER BY LENGTH(name), name ASC";

	private static final String SELECT_RECIPES = "SELECT * FROM RECIPE WHERE DELETED = FALSE;";

	private static final String SELECT_RECIPE_WHERE = "SELECT * FROM RECIPE WHERE ID = ?;";
	private static final String UPDATE_RECIPE_WHERE = "UPDATE RECIPE SET NAME = ?, DURATION = ?, DESCRIPTION = ?, TAGS = ? WHERE ID = ?;";

	private static final String DELETE_RECIPE = "UPDATE RECIPE SET DELETED = TRUE WHERE id = ?;";
	
	private static final String SELECT_R_I_WHERE = "SELECT * FROM RECIPE_INGREDIENT r_i JOIN INGREDIENT i ON r_i.INGREDIENT_ID = i.ID JOIN RECIPE r ON r_i.RECIPE_ID = r.ID WHERE r.ID = ?;";
	private static final String DELETE_R_I_WHERE = "DELETE FROM RECIPE_INGREDIENT WHERE RECIPE_ID = ?;";
	//private static final String INSERT_R_I_WHERE = "INSERT INTO RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (?, ?, ?);";

	private static final String CREATE_RECIPE_INGREDIENT = "INSERT INTO recipe_ingredient (ingredient_id, recipe_id, amount) VALUES (?,?,?);";

	@Override
	public void create(Recipe recipe) throws PersistenceException {
		LOG.debug("Creating a new Recipe {}", recipe);

		PreparedStatement createRecipe = null;
		ResultSet generatedKeys = null;

		try {
			Connection connection = JDBCConnectionManager.getConnection();
			JDBCConnectionManager.startTransaction();

			createRecipe = connection.prepareStatement(CREATE_RECIPE, Statement.RETURN_GENERATED_KEYS);
			createRecipe.setString(1, recipe.getName());
			createRecipe.setDouble(2, recipe.getDuration());

			Clob clob = connection.createClob();
			clob.setString(1, recipe.getDescription());
			createRecipe.setClob(3, clob);

			createRecipe.setString(4, recipe.getTagsAsString());
			createRecipe.setBoolean(5, false); // is deleted = false
			createRecipe.execute();

			generatedKeys = createRecipe.getGeneratedKeys();
			generatedKeys.next();

			recipe.setId(generatedKeys.getInt(1));
			LOG.debug("Created Recipe will have recipeId={}", recipe.getId());

			List<RecipeIngredient> userSpecificRecipeIngredients = recipe.getRecipeIngredients().stream()
					.filter(ri -> ri.getId() == null).collect(Collectors.toList());
			List<RecipeIngredient> commonRecipeIngredients = recipe.getRecipeIngredients().stream()
					.filter(ri -> ri.getId() != null).collect(Collectors.toList());

			for (RecipeIngredient ri : userSpecificRecipeIngredients) {
				Integer userIngredientId = createUserSpecificIngredientTuple(ri);
				createRecipeIngredientTuple(ri, userIngredientId, recipe.getId());
			}

			for (RecipeIngredient ri : commonRecipeIngredients) {
				createRecipeIngredientTuple(ri, ri.getId(), recipe.getId());
			}

			JDBCConnectionManager.commitTransaction();
		} catch (SQLException e) {
			JDBCConnectionManager.rollbackTransaction();
			throw new PersistenceException(
					"There was an error while creating a recipe in the database. " + e.getMessage());
		} finally {
			JDBCConnectionManager.finalizeTransaction();
			CloseUtil.closeResultSet(generatedKeys);
			CloseUtil.closeStatement(createRecipe);
		}
	}

	private void createRecipeIngredientTuple(RecipeIngredient ri, Integer ingredientId, Integer recipeId)
			throws SQLException {
		LOG.debug("Creating new Recipe_Ingredient tuple for ingredientId={} and recipeId={} with amount={}.",
				ingredientId, recipeId, ri.getAmount());

		PreparedStatement createRecipeIngredient = JDBCConnectionManager.getConnection()
				.prepareStatement(CREATE_RECIPE_INGREDIENT);

		createRecipeIngredient.setInt(1, ingredientId);
		createRecipeIngredient.setInt(2, recipeId);
		createRecipeIngredient.setDouble(3, ri.getAmount());

		createRecipeIngredient.execute();
	}

	private Integer createUserSpecificIngredientTuple(RecipeIngredient ri) throws SQLException {
		LOG.debug("Creating new user specific Ingredient tuple for ingredientName='{}'.", ri.getIngredientName());
		PreparedStatement createUserIngredient = JDBCConnectionManager.getConnection()
				.prepareStatement(CREATE_USER_INGREDIENT, Statement.RETURN_GENERATED_KEYS);

		createUserIngredient.setString(1, ri.getIngredientName().trim());
		createUserIngredient.setDouble(2, ri.getEnergyKcal());
		createUserIngredient.setDouble(3, ri.getLipid());
		createUserIngredient.setDouble(4, ri.getProtein());
		createUserIngredient.setDouble(5, ri.getCarbohydrate());
		createUserIngredient.setString(6, ri.getUnitName());
		createUserIngredient.setDouble(7, ri.getUnitGramNormalised());

		createUserIngredient.execute();
		ResultSet generatedKeys = createUserIngredient.getGeneratedKeys();
		generatedKeys.next();
		int userSpecIngredientId = generatedKeys.getInt(1);
		LOG.debug("Created Ingredient will have ingredientId={}", userSpecIngredientId);
		return userSpecIngredientId;
	}

	@Override
	public List<RecipeIngredient> searchIngredient(IngredientSearchParam searchIngredient) throws PersistenceException {
		LOG.debug("Searching for Ingredient with name '{}'", searchIngredient.getIngredientName());

		PreparedStatement searchIngredientStmnt = null;

		try {
			searchIngredientStmnt = JDBCConnectionManager.getConnection().prepareStatement(SEARCH_INGREDIENT);
			searchIngredientStmnt.setString(1, searchIngredient.getIngredientName().trim() + "%");
			ResultSet resultSet = searchIngredientStmnt.executeQuery();
			List<RecipeIngredient> searchResult = new ArrayList<>();

			while (resultSet.next() != false) {
				searchResult.add(transformToRecipeIngredient(resultSet));
			}
			return searchResult;
		} catch (SQLException e) {
			throw new PersistenceException("There was an error while searching for ingredient." + e.getMessage());
		} finally {
			CloseUtil.closeStatement(searchIngredientStmnt);
		}
	}

	private RecipeIngredient transformToRecipeIngredient(ResultSet resultSet) throws SQLException {
		Integer id = resultSet.getInt("ID");
		String ingredientName = resultSet.getString("NAME");
		Double energyKcal = resultSet.getDouble("ENERG_KCAL");
		Double lipid = resultSet.getDouble("LIPID");
		Double protein = resultSet.getDouble("PROTEIN");
		Double carbohydrate = resultSet.getDouble("CARBOHYDRT");
		String unitName = resultSet.getString("UNIT_NAME");
		Double unitGramNormalised = resultSet.getDouble("UNIT_GRAM_NORMALISED");
		Boolean userSpecific = resultSet.getBoolean("USER_SPECIFIC");

		return new RecipeIngredient(id, null, energyKcal, lipid, protein, carbohydrate, unitName, unitGramNormalised,
				userSpecific, ingredientName);
	}

	@Override
	public Recipe get(int id) throws PersistenceException {
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = JDBCConnectionManager.getConnection().prepareStatement(SELECT_RECIPE_WHERE);
			ps.setInt(1, id);

			rs = ps.executeQuery();

			if (rs.next()) {
				Recipe r = new Recipe(rs.getInt("ID"), rs.getString("NAME"), rs.getDouble("DURATION"),
						rs.getString("DESCRIPTION"), rs.getString("TAGS"), rs.getBoolean("DELETED"));
				r.setRecipeIngredients(getIngredients(id));
				return r;
			}

			throw new PersistenceException("No recipe found for given id");
		} catch (SQLException e) {
			throw new PersistenceException(e);
		} finally {
			CloseUtil.closeStatement(ps);
			CloseUtil.closeResultSet(rs);
		}
	}

	private List<RecipeIngredient> getIngredients(int id) throws PersistenceException {
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = JDBCConnectionManager.getConnection().prepareStatement(SELECT_R_I_WHERE);
			ps.setInt(1, id);
			rs = ps.executeQuery();

			List<RecipeIngredient> ingredients = new ArrayList<>();
			while (rs.next()) {
				ingredients.add(new RecipeIngredient(rs.getInt("INGREDIENT_ID"), rs.getDouble("AMOUNT"),
						rs.getDouble("ENERG_KCAL"), rs.getDouble("LIPID"), rs.getDouble("PROTEIN"),
						rs.getDouble("CARBOHYDRT"), rs.getString("UNIT_NAME"), rs.getDouble("UNIT_GRAM_NORMALISED"),
						rs.getBoolean("USER_SPECIFIC"), rs.getString("NAME")));
			}

			return ingredients;
		} catch (SQLException e) {
			throw new PersistenceException(e);
		} finally {
			CloseUtil.closeStatement(ps);
			CloseUtil.closeResultSet(rs);
		}
	}

	@Override
	public void update(Recipe recipe) throws PersistenceException {
		JDBCConnectionManager.startTransaction();

		PreparedStatement ps = null;
		try {
			ps = JDBCConnectionManager.getConnection().prepareStatement(UPDATE_RECIPE_WHERE);
			ps.setString(1, recipe.getName());
			ps.setDouble(2, recipe.getDuration());

			Clob description = JDBCConnectionManager.getConnection().createClob();
			description.setString(1, recipe.getDescription());
			ps.setClob(3, description);

			ps.setString(4, recipe.getTagsAsString());
			ps.setInt(5, recipe.getId());
			ps.executeUpdate();

			setIngredients(recipe);

			JDBCConnectionManager.commitTransaction();
		} catch (SQLException e) {
			JDBCConnectionManager.rollbackTransaction();
			throw new PersistenceException(e);
		} finally {
			JDBCConnectionManager.finalizeTransaction();
			CloseUtil.closeStatement(ps);
		}
	}

	private void setIngredients(Recipe recipe) throws PersistenceException {
		PreparedStatement ps = null;
		try {
			ps = JDBCConnectionManager.getConnection().prepareStatement(DELETE_R_I_WHERE);
			ps.setInt(1, recipe.getId());
			ps.executeUpdate();

            List<RecipeIngredient> newUserSpecificRecipeIngredients = recipe.getRecipeIngredients().stream()
                .filter(ri -> ri.getId() == null).collect(Collectors.toList());
            List<RecipeIngredient> commonRecipeIngredients = recipe.getRecipeIngredients().stream()
                .filter(ri -> ri.getId() != null).collect(Collectors.toList());

            for (RecipeIngredient ri : newUserSpecificRecipeIngredients) {
                Integer userIngredientId = createUserSpecificIngredientTuple(ri);
                createRecipeIngredientTuple(ri, userIngredientId, recipe.getId());
            }

            for (RecipeIngredient ri : commonRecipeIngredients)
                createRecipeIngredientTuple(ri, ri.getId(), recipe.getId());

		} catch (SQLException e) {
			JDBCConnectionManager.rollbackTransaction();
			throw new PersistenceException(e);
		} finally {
			CloseUtil.closeStatement(ps);
		}
	}

	@Override
	public List<Recipe> getRecipes() throws PersistenceException {
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = JDBCConnectionManager.getConnection().prepareStatement(SELECT_RECIPES);
			rs = ps.executeQuery();

			List<Recipe> recipes = new ArrayList<>();
			while (rs.next()) {
				Recipe r = new Recipe(rs.getInt("ID"), rs.getString("NAME"), rs.getDouble("DURATION"),
						rs.getString("DESCRIPTION"), rs.getString("TAGS"), rs.getBoolean("DELETED"));
				r.setRecipeIngredients(getIngredients(r.getId()));
				recipes.add(r);
			}

			return recipes;
		} catch (SQLException e) {
			throw new PersistenceException(e);
		} finally {
			CloseUtil.closeStatement(ps);
			CloseUtil.closeResultSet(rs);
		}
	}

	@Override
	public void delete(int id) throws PersistenceException {
		LOG.debug("Deleting recipe with ID {}");

		PreparedStatement createRecipe = null;

		try {
			Connection connection = JDBCConnectionManager.getConnection();
			JDBCConnectionManager.startTransaction();

			createRecipe = connection.prepareStatement(DELETE_RECIPE);
			createRecipe.setInt(1, id);
			
			if (createRecipe.executeUpdate() == 0) {
				throw new PersistenceException("No recipe found for given id");
			}

			JDBCConnectionManager.commitTransaction();
		} catch (SQLException e) {
			JDBCConnectionManager.rollbackTransaction();
			throw new PersistenceException(
					"There was an error while deleting the recipe in the database. " + e.getMessage());
		} finally {
			JDBCConnectionManager.finalizeTransaction();
			CloseUtil.closeStatement(createRecipe);
		}
	}
}