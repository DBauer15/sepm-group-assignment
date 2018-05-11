package at.ac.tuwien.sepm.assignment.groupphase.application.persistence.implementation;

import at.ac.tuwien.sepm.assignment.groupphase.application.dto.DietPlan;
import at.ac.tuwien.sepm.assignment.groupphase.application.dto.IngredientSearchParam;
import at.ac.tuwien.sepm.assignment.groupphase.application.dto.Recipe;
import at.ac.tuwien.sepm.assignment.groupphase.application.dto.RecipeIngredient;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.NoEntryFoundException;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.RecipePersistence;
import at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation.CloseUtil;
import at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation.JDBCConnectionManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.lang.invoke.MethodHandles;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DBRecipePersistence implements RecipePersistence {
	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private static final String CREATE_RECIPE = "INSERT INTO RECIPE (name, duration, description, tags, deleted) VALUES (?, ?, ?, ?, ?)";
	private static final String SEARCH_INGREDIENT = "SELECT ID, NAME, ENERG_KCAL, LIPID, PROTEIN, CARBOHYDRT, "
			+ "UNIT_NAME, UNIT_GRAM_NORMALISED, USER_SPECIFIC FROM ingredient WHERE name ILIKE ? ORDER BY LENGTH(name), name ASC";
    private static final String SELECT_RECIPES = "SELECT * FROM RECIPE WHERE DELETED = FALSE;";
    private static final String SELECT_RECIPE_WHERE = "SELECT * FROM RECIPE WHERE ID = ?;";
    private static final String SELECT_INGREDIENTS_WHERE = "SELECT * FROM RECIPE_INGREDIENT r_i JOIN INGREDIENT i ON r_i.INGREDIENT_ID = i.ID JOIN RECIPE r ON r_i.RECIPE_ID = r.ID WHERE r.ID = ?;";
    private static final String UPDATE_RECIPE_WHERE = "UPDATE RECIPE SET NAME = ?, DURATION = ?, DESCRIPTION = ?, TAGS = ?, DELETED = ? WHERE ID = ?;";

	private PreparedStatement ps;

	@Override
	public void create(Recipe recipe) throws PersistenceException {
		LOG.debug("Creating a new Recipe {}", recipe);

		ResultSet generatedKeys = null;
		int i = 1;

		try {
			Connection connection = JDBCConnectionManager.getConnection();
			ps = connection.prepareStatement(CREATE_RECIPE, Statement.RETURN_GENERATED_KEYS);
			ps.setString(i++, recipe.getName());
			ps.setDouble(i++, recipe.getDuration());

			Clob clob = connection.createClob();
			clob.setString(1, recipe.getDescription());

			ps.setClob(i++, clob);
			ps.setString(i++, recipe.getTagsAsString());
			ps.setBoolean(i++, false);
			ps.execute();

			generatedKeys = ps.getGeneratedKeys();
			generatedKeys.next();

			recipe.setId(generatedKeys.getInt(1));
		} catch (SQLException e) {
			throw new PersistenceException(
					"There was an error while creating a recipe in the database. " + e.getMessage());
		} finally {
			CloseUtil.closeResultSet(generatedKeys);
			CloseUtil.closeStatement(ps);
		}
	}

	@Override
	public List<RecipeIngredient> searchIngredient(IngredientSearchParam searchIngredient) throws PersistenceException {
		LOG.debug("Searching for Ingredient with name '{}'", searchIngredient.getIngredientName());

		PreparedStatement searchIngredientStmnt = null;

		try {
			searchIngredientStmnt = JDBCConnectionManager.getConnection().prepareStatement(SEARCH_INGREDIENT);
			searchIngredientStmnt.setString(1, searchIngredient.getIngredientName().trim()+"%");
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
                Recipe r = new Recipe(
                    rs.getInt("ID"),
                    rs.getString("NAME"),
                    rs.getDouble("DURATION"),
                    rs.getString("DESCRIPTION"),
                    rs.getString("TAGS"),
                    rs.getBoolean("DELETED"));
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
            ps = JDBCConnectionManager.getConnection().prepareStatement(SELECT_INGREDIENTS_WHERE);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            List<RecipeIngredient> ingredients = new ArrayList<>();
            while (rs.next()) {
                ingredients.add(new RecipeIngredient(
                    rs.getInt("INGREDIENT_ID"),
                    rs.getDouble("AMOUNT"),
                    rs.getDouble("ENERG_KCAL"),
                    rs.getDouble("LIPID"),
                    rs.getDouble("PROTEIN"),
                    rs.getDouble("CARBOHYDRT"),
                    rs.getString("UNIT_NAME"),
                    rs.getDouble("UNIT_GRAM_NORMALISED"),
                    rs.getBoolean("USER_SPECIFIC"),
                    rs.getString("NAME")
                ));
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
        PreparedStatement ps = null;
        try {
            ps = JDBCConnectionManager.getConnection().prepareStatement(UPDATE_RECIPE_WHERE);
            ps.setString(1, recipe.getName());
            ps.setDouble(2, recipe.getDuration());

            Clob description = JDBCConnectionManager.getConnection().createClob();
            description.setString(1, recipe.getDescription());
            ps.setClob(3, description);

            ps.setString(4, recipe.getTagsAsString());
            ps.setBoolean(5, recipe.getDeleted());
            ps.setInt(6, recipe.getId());
            ps.executeQuery();
        } catch (SQLException e) {
            throw new PersistenceException(e);
        } finally {
            CloseUtil.closeStatement(ps);
        }
    }

    @Override
    public List<Recipe> list() throws PersistenceException {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = JDBCConnectionManager.getConnection().prepareStatement(SELECT_RECIPES);
            rs = ps.executeQuery();

            List<Recipe> recipes = new ArrayList<>();
            while (rs.next()) {
                Recipe r = new Recipe(
                    rs.getInt("ID"),
                    rs.getString("NAME"),
                    rs.getDouble("DURATION"),
                    rs.getString("DESCRIPTION"),
                    rs.getString("TAGS"),
                    rs.getBoolean("DELETED"));
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
}