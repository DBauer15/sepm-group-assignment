package at.ac.tuwien.sepm.assignment.groupphase.application.persistence.implementation;

import at.ac.tuwien.sepm.assignment.groupphase.application.dto.DietPlan;
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
	public List<RecipeIngredient> searchIngredient(String query) throws PersistenceException {
		LOG.debug("Searching for Ingredient with name '{}'", query);

		PreparedStatement searchIngredientStmnt = null;

		try {
			searchIngredientStmnt = JDBCConnectionManager.getConnection().prepareStatement(SEARCH_INGREDIENT);
			searchIngredientStmnt.setString(1, query+"%");
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

}