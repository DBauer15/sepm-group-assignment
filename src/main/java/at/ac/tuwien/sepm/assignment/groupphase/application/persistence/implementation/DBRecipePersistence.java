
package at.ac.tuwien.sepm.assignment.groupphase.application.persistence.implementation;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import at.ac.tuwien.sepm.assignment.groupphase.application.dto.IngredientSearchParam;
import at.ac.tuwien.sepm.assignment.groupphase.application.dto.Recipe;
import at.ac.tuwien.sepm.assignment.groupphase.application.dto.RecipeImage;
import at.ac.tuwien.sepm.assignment.groupphase.application.dto.RecipeIngredient;
import at.ac.tuwien.sepm.assignment.groupphase.application.dto.RecipeSearchParam;
import at.ac.tuwien.sepm.assignment.groupphase.application.dto.RecipeTag;
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
	private static final String SEARCH_INGREDIENT = "SELECT * FROM ingredient WHERE name ILIKE ? ORDER BY LENGTH(name), name ASC;";

	private static final String SELECT_RECIPES = "SELECT * FROM RECIPE WHERE DELETED = FALSE;";

	private static final String SELECT_RECIPE_WHERE = "SELECT * FROM RECIPE WHERE ID = ?;";
	private static final String UPDATE_RECIPE_WHERE = "UPDATE RECIPE SET NAME = ?, DURATION = ?, DESCRIPTION = ?, TAGS = ? WHERE ID = ?;";

	private static final String DELETE_RECIPE = "UPDATE RECIPE SET DELETED = TRUE WHERE id = ?;";

	private static final String SELECT_R_I_WHERE = "SELECT * FROM RECIPE_INGREDIENT r_i JOIN INGREDIENT i ON r_i.INGREDIENT_ID = i.ID JOIN RECIPE r ON r_i.RECIPE_ID = r.ID WHERE r.ID = ?;";
	private static final String DELETE_R_I_WHERE = "DELETE FROM RECIPE_INGREDIENT WHERE RECIPE_ID = ?;";
	// private static final String INSERT_R_I_WHERE = "INSERT INTO RECIPE_INGREDIENT
	// (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (?, ?, ?);";

	private static final String SELECT_RECIPE_IMAGES = "SELECT * FROM RECIPE_IMAGE WHERE RECIPE_id = ?";

	private static final String CREATE_RECIPE_INGREDIENT = "INSERT INTO recipe_ingredient (ingredient_id, recipe_id, amount) VALUES (?,?,?);";
	private static final String CREATE_RECIPE_IMAGE = "INSERT INTO Recipe_Image (recipe_id, image, image_type) VALUES (?, ?, ?);";
	private static final String DELETE_RECIPE_IMAGES = "DELETE FROM Recipe_Image WHERE recipe_id = ?;";
	
	private static final String IS_RECIPE_CURRENTLY_SUGGESTED = "SELECT 1 FROM diet_plan_suggestion x WHERE recipe = ? AND date = TRUNC(NOW()) AND NOT EXISTS (SELECT 1 FROM diet_plan_suggestion WHERE tag = x.tag AND date = x.date AND created_timestamp > x.created_timestamp)";

	private static final String SEARCH_RECIPES = "select r.* from recipe r " + //
			"WHERE (? IS NULL OR EXISTS (select 1 from recipe_ingredient ri inner join ingredient i on i.id = ri.ingredient_id WHERE i.name ILIKE '%' || ? || '%' AND r.id = ri.recipe_id)) "
			+ "AND (? IS NULL OR EXISTS (select 1 from recipe_ingredient ri inner join ingredient i on i.id = ri.ingredient_id WHERE i.name ILIKE '%' || ? || '%' AND r.id = ri.recipe_id)) "
			+ "AND (? IS NULL OR EXISTS (select 1 from recipe_ingredient ri inner join ingredient i on i.id = ri.ingredient_id WHERE i.name ILIKE '%' || ? || '%' AND r.id = ri.recipe_id)) "
			+ "AND (? IS NULL OR EXISTS (select 1 from recipe_ingredient ri inner join ingredient i on i.id = ri.ingredient_id WHERE i.name ILIKE '%' || ? || '%' AND r.id = ri.recipe_id)) "
			+ "AND (? IS NULL OR EXISTS (select 1 from recipe_ingredient ri inner join ingredient i on i.id = ri.ingredient_id WHERE i.name ILIKE '%' || ? || '%' AND r.id = ri.recipe_id)) "
			+ "AND (? IS NULL OR EXISTS (select 1 from recipe_ingredient ri inner join ingredient i on i.id = ri.ingredient_id WHERE i.name ILIKE '%' || ? || '%' AND r.id = ri.recipe_id)) "
			+ "AND (? IS NULL OR EXISTS (select 1 from recipe_ingredient ri inner join ingredient i on i.id = ri.ingredient_id WHERE i.name ILIKE '%' || ? || '%' AND r.id = ri.recipe_id)) "
			+ "AND (? IS NULL OR EXISTS (select 1 from recipe_ingredient ri inner join ingredient i on i.id = ri.ingredient_id WHERE i.name ILIKE '%' || ? || '%' AND r.id = ri.recipe_id)) "
			+ "AND (? IS NULL OR EXISTS (select 1 from recipe_ingredient ri inner join ingredient i on i.id = ri.ingredient_id WHERE i.name ILIKE '%' || ? || '%' AND r.id = ri.recipe_id)) "
			+ "AND (? IS NULL OR EXISTS (select 1 from recipe_ingredient ri inner join ingredient i on i.id = ri.ingredient_id WHERE i.name ILIKE '%' || ? || '%' AND r.id = ri.recipe_id)) "
			+ "AND (? IS NULL OR r.name ILIKE '%' || ? || '%') " + // recipe name
			"AND (? IS NULL OR r.tags ILIKE '%' || ? || '%') " + // recipe tags
			"AND (? IS NULL OR r.tags ILIKE '%' || ? || '%') " + // recipe tags
			"AND (? IS NULL OR r.tags ILIKE '%' || ? || '%') " + // recipe tags
			"AND (? IS NULL OR r.duration >= ?) " + // recipe duration lower incl bound
			"AND (? IS NULL OR r.duration <= ?) "; // recipe duration upper incl bound

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

			for (RecipeImage ri : recipe.getRecipeImages()) {
				createRecipeImage(ri, recipe.getId());
			}

			JDBCConnectionManager.commitTransaction();
		} catch (SQLException | IOException e) {
			JDBCConnectionManager.rollbackTransaction();
			throw new PersistenceException(
					"There was an error while creating a recipe in the database. " + e.getMessage(), e);
		} finally {
			JDBCConnectionManager.finalizeTransaction();
			CloseUtil.closeResultSet(generatedKeys);
			CloseUtil.closeStatement(createRecipe);
		}
	}

	private void createRecipeImage(RecipeImage ri, Integer recipeId) throws SQLException, IOException {
		LOG.debug("Creating new Recipe_Image for recipeId={}.", recipeId);

		PreparedStatement createRecipeImage = JDBCConnectionManager.getConnection()
				.prepareStatement(CREATE_RECIPE_IMAGE, Statement.RETURN_GENERATED_KEYS);

		createRecipeImage.setInt(1, recipeId);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(ri.getImage(), ri.getImageType(), baos);
		Blob blob = new javax.sql.rowset.serial.SerialBlob(baos.toByteArray());
		        
		createRecipeImage.setBlob(2, blob);
		createRecipeImage.setString(3, ri.getImageType());
		createRecipeImage.execute();
		
		ResultSet generatedKeys = createRecipeImage.getGeneratedKeys();
		generatedKeys.next();
		
		ri.setId(generatedKeys.getInt(1));
				
		LOG.debug("Created image for recipe {} with ID {}", recipeId, ri.getId());
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
		ResultSet resultSet = null;

		try {
			searchIngredientStmnt = JDBCConnectionManager.getConnection()
					.prepareStatement(SEARCH_INGREDIENT.replace("?",
							"'%" + searchIngredient.getIngredientName().trim().replaceAll("\\s", "%' AND name ILIKE '%")
									+ "%'"));
			resultSet = searchIngredientStmnt.executeQuery();
			List<RecipeIngredient> searchResult = new ArrayList<>();

			while (resultSet.next()) {
				searchResult.add(transformToRecipeIngredient(resultSet));
			}
			return searchResult;
		} catch (SQLException e) {
			throw new PersistenceException("There was an error while searching for ingredient." + e.getMessage(), e);
		} finally {
			CloseUtil.closeStatement(searchIngredientStmnt);
			CloseUtil.closeResultSet(resultSet);
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
				r.setRecipeImages(getImages(id));
				return r;
			}

			throw new PersistenceException("No recipe found for given id");
		} catch (SQLException e) {
			throw new PersistenceException(e.getMessage(), e);
		} finally {
			CloseUtil.closeStatement(ps);
			CloseUtil.closeResultSet(rs);
		}
	}

	private List<RecipeImage> getImages(int id) throws PersistenceException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		InputStream in = null;

		try {
			ps = JDBCConnectionManager.getConnection().prepareStatement(SELECT_RECIPE_IMAGES);
			ps.setInt(1, id);
			rs = ps.executeQuery();

			List<RecipeImage> recipeImages = new ArrayList<>();

			while (rs.next()) {
				Blob blob = rs.getBlob("image");
				in = blob.getBinaryStream();  
	        	BufferedImage image = ImageIO.read(in);
	        	 
				recipeImages.add(new RecipeImage(rs.getInt("id"), image, rs.getString("image_type")));
			}

			return recipeImages;
		} catch (SQLException | IOException e) {
			throw new PersistenceException(e.getMessage(), e);
		} finally {
			CloseUtil.closeInputStream(in);
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
			throw new PersistenceException(e.getMessage(), e);
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
			setImages(recipe);

			JDBCConnectionManager.commitTransaction();
		} catch (SQLException e) {
			JDBCConnectionManager.rollbackTransaction();
			throw new PersistenceException(e.getMessage(), e);
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
			throw new PersistenceException(e.getMessage(), e);
		} finally {
			CloseUtil.closeStatement(ps);
		}
	}

	private void setImages(Recipe recipe) throws PersistenceException {
		PreparedStatement ps = null;
		
		try {
			ps = JDBCConnectionManager.getConnection().prepareStatement(DELETE_RECIPE_IMAGES);
			ps.setInt(1, recipe.getId());
			ps.executeUpdate();

			for (RecipeImage ri : recipe.getRecipeImages()) {
				createRecipeImage(ri, recipe.getId());
			}	
		} catch (SQLException | IOException e) {
			JDBCConnectionManager.rollbackTransaction();
			throw new PersistenceException(e.getMessage(), e);
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
				r.setRecipeImages(getImages(r.getId()));
				recipes.add(r);
			}

			return recipes;
		} catch (SQLException e) {
			throw new PersistenceException(e.getMessage(), e);
		} finally {
			CloseUtil.closeStatement(ps);
			CloseUtil.closeResultSet(rs);
		}
	}

	@Override
	public void delete(int id) throws PersistenceException {
		LOG.debug("Deleting recipe with ID {}");

		PreparedStatement createRecipe = null;
		PreparedStatement isRecipeCurrentlySuggested = null;
		ResultSet rs = null;

		try {
			Connection connection = JDBCConnectionManager.getConnection();
			JDBCConnectionManager.startTransaction();

			isRecipeCurrentlySuggested = connection.prepareStatement(IS_RECIPE_CURRENTLY_SUGGESTED);
			isRecipeCurrentlySuggested.setInt(1, id);

			rs = isRecipeCurrentlySuggested.executeQuery();

			if (rs.next()) {
				throw new PersistenceException(
						"The recipe has been suggested for today. You must change today's recipe proposal before you can delete the recipe.");
			}

			createRecipe = connection.prepareStatement(DELETE_RECIPE);
			createRecipe.setInt(1, id);

			if (createRecipe.executeUpdate() == 0) {
				throw new PersistenceException("No recipe found for given id");
			}

			JDBCConnectionManager.commitTransaction();
		} catch (SQLException e) {
			JDBCConnectionManager.rollbackTransaction();
			throw new PersistenceException(
					"There was an error while deleting the recipe in the database. " + e.getMessage(), e);
		} finally {
			JDBCConnectionManager.finalizeTransaction();
			CloseUtil.closeResultSet(rs);
			CloseUtil.closeStatement(isRecipeCurrentlySuggested);
			CloseUtil.closeStatement(createRecipe);
		}
	}

	@Override
	public List<Recipe> searchRecipes(RecipeSearchParam searchParam) throws PersistenceException {
		LOG.debug("Searching Recipes with search criteria");

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = JDBCConnectionManager.getConnection().prepareStatement(SEARCH_RECIPES);

			// set all 10 ingredients if available or null if not
			Iterator<String> ingredientIterator = searchParam.getIngredients().iterator();

			final int maxIngredientIdx = 20;
			for (int i = 1; i < maxIngredientIdx; i = i + 2) {
				// sets 2 same parameters for condition:
				// (? IS NULL OR i.name ILIKE '%' || ? || '%')
				if (ingredientIterator.hasNext() == false) {
					ps.setNull(i, Types.VARCHAR);
					ps.setNull(i + 1, Types.VARCHAR);
				} else {
					final String nextIngredient = ingredientIterator.next();
					ps.setString(i, nextIngredient);
					ps.setString(i + 1, nextIngredient);
				}
			}
			// assert: last parameter index
			int paramIdx = maxIngredientIdx;

			// recipe name
			if (searchParam.getRecipeName() != null) {
				ps.setString(++paramIdx, searchParam.getRecipeName());
				ps.setString(++paramIdx, searchParam.getRecipeName());
			} else {
				ps.setNull(++paramIdx, Types.VARCHAR);
				ps.setNull(++paramIdx, Types.VARCHAR);
			}

			// tags
			Iterator<RecipeTag> tagIterator = null;
			if (searchParam.getTags() != null) {
				tagIterator = searchParam.getTags().iterator();
			}
			for (int i = 0; i < 3; i++) {
				if (tagIterator != null && tagIterator.hasNext() == true) {
					final RecipeTag nextTag = tagIterator.next();
					ps.setString(++paramIdx, nextTag.toString());
					ps.setString(++paramIdx, nextTag.toString());
				} else {
					ps.setNull(++paramIdx, Types.VARCHAR);
					ps.setNull(++paramIdx, Types.VARCHAR);
				}
			}
			
			
			// duration
			if (searchParam.getLowerDurationInkl() != null) {
				ps.setDouble(++paramIdx, searchParam.getLowerDurationInkl());
				ps.setDouble(++paramIdx, searchParam.getLowerDurationInkl());
			} else {
				ps.setNull(++paramIdx, Types.DOUBLE);
				ps.setNull(++paramIdx, Types.DOUBLE);
			}
			if (searchParam.getUpperDurationInkl() != null) {
				ps.setDouble(++paramIdx, searchParam.getUpperDurationInkl());
				ps.setDouble(++paramIdx, searchParam.getUpperDurationInkl());
			} else {
				ps.setNull(++paramIdx, Types.DOUBLE);
				ps.setNull(++paramIdx, Types.DOUBLE);
			}

			rs = ps.executeQuery();

			List<Recipe> recipes = new ArrayList<>();
			while (rs.next()) {
				Recipe r = new Recipe(rs.getInt("ID"), rs.getString("NAME"), rs.getDouble("DURATION"),
						rs.getString("DESCRIPTION"), rs.getString("TAGS"), rs.getBoolean("DELETED"));
				r.setRecipeIngredients(getIngredients(r.getId()));
				r.setRecipeImages(getImages(r.getId()));
				recipes.add(r);
			}

			return recipes;
		} catch (SQLException e) {
			throw new PersistenceException(e.getMessage(), e);
		} finally {
			CloseUtil.closeStatement(ps);
			CloseUtil.closeResultSet(rs);
		}
	}
}