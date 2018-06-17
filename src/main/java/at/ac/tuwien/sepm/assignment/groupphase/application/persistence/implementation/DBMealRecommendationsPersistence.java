package at.ac.tuwien.sepm.assignment.groupphase.application.persistence.implementation;

import at.ac.tuwien.sepm.assignment.groupphase.application.dto.DietPlan;
import at.ac.tuwien.sepm.assignment.groupphase.application.dto.Recipe;
import at.ac.tuwien.sepm.assignment.groupphase.application.dto.RecipeTag;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.MealRecommendationsPersistence;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.NoEntryFoundException;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.RecipePersistence;
import at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation.CloseUtil;
import at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation.JDBCConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.lang.invoke.MethodHandles;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class DBMealRecommendationsPersistence implements MealRecommendationsPersistence {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final String SQL_CREATE_MEAL_RECOMMENDATION = "INSERT INTO diet_plan_suggestion (recipe, date, tag, diet_plan_id, created_timestamp) VALUES (?,CURDATE(),?,?,NOW())";
    private static final String SQL_READ_MEAL_RECOMMANDATION = "SELECT * FROM diet_plan_suggestion WHERE diet_plan_id=? AND tag=? AND date=CURDATE() AND created_timestamp = " +
        "(SELECT max(created_timestamp) FROM diet_plan_suggestion WHERE diet_plan_id=? AND tag=?)";

    private RecipePersistence recipePersistence;

    public DBMealRecommendationsPersistence(RecipePersistence recipePersistence) {
        this.recipePersistence = recipePersistence;
    }

    @Override
    public void createRecommendationFor(Recipe recipe, DietPlan dietPlan, RecipeTag recipeTag) throws PersistenceException {
        LOG.debug("Creating diet plan suggestion for {} with tag {}", recipe, recipeTag);

        PreparedStatement createRecommendationStmt = null;
        try {
            createRecommendationStmt = JDBCConnectionManager.getConnection().prepareStatement(SQL_CREATE_MEAL_RECOMMENDATION);

            createRecommendationStmt.setInt(1, recipe.getId());
            createRecommendationStmt.setString(2, recipeTag.toString());
            createRecommendationStmt.setInt(3, dietPlan.getId());

            createRecommendationStmt.executeUpdate();

            LOG.debug("Successfully created diet plan suggestion");
        } catch (SQLException e) {
            throw new PersistenceException("There was an error while creating a diet plan suggestion in the database. " + e.getMessage(), e);
        } finally {
            CloseUtil.closeStatement(createRecommendationStmt);
        }
    }

    @Override
    public Recipe readRecommendationFor(DietPlan dietPlan, RecipeTag recipeTag) throws PersistenceException, NoEntryFoundException {
        LOG.debug("Reading a diet plan suggestion for {} with tag {}", dietPlan, recipeTag);

        PreparedStatement readRecommendationStmt = null;
        ResultSet readResult = null;

        try {
            readRecommendationStmt = JDBCConnectionManager.getConnection().prepareStatement(SQL_READ_MEAL_RECOMMANDATION);

            readRecommendationStmt.setInt(1, dietPlan.getId());
            readRecommendationStmt.setString(2, recipeTag.toString());
            readRecommendationStmt.setInt(3, dietPlan.getId());
            readRecommendationStmt.setString(4, recipeTag.toString());

            readResult = readRecommendationStmt.executeQuery();

            if (readResult.next()) {
                int recipe_id = readResult.getInt("recipe");

                Recipe recipe = recipePersistence.get(recipe_id);

                LOG.debug("Successfully read a diet plan suggestion.");
                return recipe;
            }

            throw new NoEntryFoundException("No recipe found for given diet plan and tag");

        } catch (SQLException e) {
            throw new PersistenceException("There was an error while reading a diet plan suggestion from the database. " + e.getMessage(), e);
        } finally {
            CloseUtil.closeStatement(readRecommendationStmt);
            CloseUtil.closeResultSet(readResult);
        }
    }
}
