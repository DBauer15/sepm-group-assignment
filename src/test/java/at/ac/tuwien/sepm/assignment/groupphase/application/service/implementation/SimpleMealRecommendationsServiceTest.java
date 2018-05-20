package at.ac.tuwien.sepm.assignment.groupphase.application.service.implementation;

import at.ac.tuwien.sepm.assignment.groupphase.application.dto.DietPlan;
import at.ac.tuwien.sepm.assignment.groupphase.application.dto.Recipe;
import at.ac.tuwien.sepm.assignment.groupphase.application.dto.RecipeIngredient;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.DietPlanPersistence;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.NoEntryFoundException;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.RecipePersistence;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.implementation.DBDietPlanPersistence;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.implementation.DBRecipePersistence;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.MealRecommendationsService;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.NoOptimalSolutionException;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.ServiceInvokationException;
import at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation.CloseUtil;
import at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation.JDBCConnectionManager;
import at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation.NutritionUtil;
import org.junit.Assert;
import org.junit.Test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test class for {@link SimpleMealRecommendationsService}
 */
public class SimpleMealRecommendationsServiceTest {

    //mocking
    private final RecipePersistence mockedRecipeRepo = mock(DBRecipePersistence.class);
    private final DietPlanPersistence mockedDietPlanRepo = mock(DBDietPlanPersistence.class);

    //SQL strings for prepared statements
    private static final String SQL_SELECT_ALL_RECIPES = "SELECT * FROM RECIPE WHERE DELETED = FALSE;";
    private static final String SELECT_R_I_WHERE = "SELECT * FROM RECIPE_INGREDIENT r_i JOIN INGREDIENT i ON r_i.INGREDIENT_ID = i.ID JOIN RECIPE r ON r_i.RECIPE_ID = r.ID WHERE r.ID = ?;";

    @Test
    public void testGetRecommendedMeals_optimalSolutionPossible_returnedRecommendedMealsNotEmpty() throws PersistenceException, NoEntryFoundException, ServiceInvokationException {
        //mock our plan Build Muscle
        DietPlan mockedActiveDietplan = new DietPlan(1, "Testplan", 2500.0, 20.0, 25.0, 50.0, LocalDate.now(), null);
        when(mockedDietPlanRepo.readActive()).thenReturn(mockedActiveDietplan);

        //mock all recipes in test db
        List<Recipe> allRecipes = getRecipes();
        allRecipes.forEach(NutritionUtil::fillNutritionValues);
        when(mockedRecipeRepo.getRecipes()).thenReturn(allRecipes);

        MealRecommendationsService mealRecommendationsService = new SimpleMealRecommendationsService(mockedRecipeRepo, mockedDietPlanRepo);
        try {
            Assert.assertFalse(mealRecommendationsService.getRecommendedMeals().isEmpty());
        } catch (NoOptimalSolutionException e) {
            Assert.fail();
        }
    }

    @Test (expected = NoOptimalSolutionException.class)
    public void testGetRecommendedMeals_noOptimumBecauseOfPlan_throwException() throws PersistenceException, NoEntryFoundException, ServiceInvokationException, NoOptimalSolutionException {
        //mock a plan where there cannot be optimal recipes
        DietPlan mockedActiveDietplan = new DietPlan(1, "Testplan", 0.0, 0.0, 0.0, 0.0, LocalDate.now(), null);
        when(mockedDietPlanRepo.readActive()).thenReturn(mockedActiveDietplan);

        //mock all recipes in test db
        List<Recipe> allRecipes = getRecipes();
        allRecipes.forEach(NutritionUtil::fillNutritionValues);
        when(mockedRecipeRepo.getRecipes()).thenReturn(allRecipes);

        MealRecommendationsService mealRecommendationsService = new SimpleMealRecommendationsService(mockedRecipeRepo, mockedDietPlanRepo);
        mealRecommendationsService.getRecommendedMeals();
    }

    @Test (expected = NoOptimalSolutionException.class)
    public void testGetRecommendedMeals_noOptimumBecauseNoRecipes_throwException() throws PersistenceException, NoEntryFoundException, ServiceInvokationException, NoOptimalSolutionException {
        //mock our plan Build Muscle
        DietPlan mockedActiveDietplan = new DietPlan(1, "Testplan", 2500.0, 20.0, 25.0, 50.0, LocalDate.now(), null);
        when(mockedDietPlanRepo.readActive()).thenReturn(mockedActiveDietplan);

        MealRecommendationsService mealRecommendationsService = new SimpleMealRecommendationsService(mockedRecipeRepo, mockedDietPlanRepo);
        mealRecommendationsService.getRecommendedMeals();
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

    public List<Recipe> getRecipes() throws PersistenceException {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = JDBCConnectionManager.getConnection().prepareStatement(SQL_SELECT_ALL_RECIPES);
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

}
