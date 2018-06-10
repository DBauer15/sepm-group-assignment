package at.ac.tuwien.sepm.assignment.groupphase.application.service.implementation;

import at.ac.tuwien.sepm.assignment.groupphase.application.dto.DietPlan;
import at.ac.tuwien.sepm.assignment.groupphase.application.dto.Recipe;
import at.ac.tuwien.sepm.assignment.groupphase.application.dto.RecipeIngredient;
import at.ac.tuwien.sepm.assignment.groupphase.application.dto.RecipeTag;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.*;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.implementation.DBDietPlanPersistence;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.implementation.DBMealRecommendationsPersistence;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.implementation.DBRecipePersistence;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.MealRecommendationsService;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.NoOptimalSolutionException;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.RecipeService;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.ServiceInvokationException;
import at.ac.tuwien.sepm.assignment.groupphase.application.util.BaseTest;
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

import static org.mockito.Mockito.*;

/**
 * Test class for {@link SimpleMealRecommendationsService}
 */
public class SimpleMealRecommendationsServiceTest extends BaseTest {

    //SQL strings for prepared statements
    private static final String SQL_SELECT_ALL_RECIPES = "SELECT * FROM RECIPE WHERE DELETED = FALSE;";
    private static final String SELECT_R_I_WHERE = "SELECT * FROM RECIPE_INGREDIENT r_i JOIN INGREDIENT i ON r_i.INGREDIENT_ID = i.ID JOIN RECIPE r ON r_i.RECIPE_ID = r.ID WHERE r.ID = ?;";
    //mocking
    private final MealRecommendationsPersistence mockedMealRecommendationRepo = mock(DBMealRecommendationsPersistence.class);
    private final RecipePersistence mockedRecipeRepo = mock(DBRecipePersistence.class);
    private final DietPlanPersistence mockedDietPlanRepo = mock(DBDietPlanPersistence.class);

    @Test
    public void testGetRecommendedMeals_buildMuscle_returnedRecommendedMealsNotEmpty() throws PersistenceException, NoEntryFoundException, ServiceInvokationException {
        //mock our plan Build Muscle
        DietPlan mockedActiveDietplan = new DietPlan(1, "Build Muscle", 2500.0, 20.0, 25.0, 50.0, LocalDate.now(), null);
        when(mockedDietPlanRepo.readActive()).thenReturn(mockedActiveDietplan);

        //mock all recipes in test db
        List<Recipe> allRecipes = getRecipes();
        allRecipes.forEach(NutritionUtil::fillNutritionValues);
        when(mockedRecipeRepo.getRecipes()).thenReturn(allRecipes);

        when(mockedMealRecommendationRepo.readRecommendationFor(mockedActiveDietplan, RecipeTag.B)).thenThrow(NoEntryFoundException.class);
        when(mockedMealRecommendationRepo.readRecommendationFor(mockedActiveDietplan, RecipeTag.L)).thenThrow(NoEntryFoundException.class);
        when(mockedMealRecommendationRepo.readRecommendationFor(mockedActiveDietplan, RecipeTag.D)).thenThrow(NoEntryFoundException.class);

        RecipeService recipeService = new SimpleRecipeService(mockedRecipeRepo);
        MealRecommendationsService mealRecommendationsService = new SimpleMealRecommendationsService(mockedMealRecommendationRepo, recipeService, mockedDietPlanRepo);
        try {
            var v = mealRecommendationsService.getRecommendedMeals();
            Assert.assertFalse(mealRecommendationsService.getRecommendedMeals().isEmpty());
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testGetRecommendedMeals_loseWeight_returnedRecommendedMealsNotEmpty() throws PersistenceException, NoEntryFoundException, ServiceInvokationException {
        //mock our plan Build Muscle
        DietPlan mockedActiveDietplan = new DietPlan(1, "Lose Weight", 2000.0, 15.0, 40.0, 45.0, LocalDate.now(), null);
        when(mockedDietPlanRepo.readActive()).thenReturn(mockedActiveDietplan);

        //mock all recipes in test db
        List<Recipe> allRecipes = getRecipes();
        allRecipes.forEach(NutritionUtil::fillNutritionValues);
        when(mockedRecipeRepo.getRecipes()).thenReturn(allRecipes);

        when(mockedMealRecommendationRepo.readRecommendationFor(mockedActiveDietplan, RecipeTag.B)).thenThrow(NoEntryFoundException.class);
        when(mockedMealRecommendationRepo.readRecommendationFor(mockedActiveDietplan, RecipeTag.L)).thenThrow(NoEntryFoundException.class);
        when(mockedMealRecommendationRepo.readRecommendationFor(mockedActiveDietplan, RecipeTag.D)).thenThrow(NoEntryFoundException.class);

        RecipeService recipeService = new SimpleRecipeService(mockedRecipeRepo);
        MealRecommendationsService mealRecommendationsService = new SimpleMealRecommendationsService(mockedMealRecommendationRepo, recipeService, mockedDietPlanRepo);
        try {
            Assert.assertFalse(mealRecommendationsService.getRecommendedMeals().isEmpty());
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testGetRecommendedMeals_carefree_returnedRecommendedMealsNotEmpty() throws PersistenceException, NoEntryFoundException, ServiceInvokationException {
        //mock our plan Build Muscle
        DietPlan mockedActiveDietplan = new DietPlan(1, "Carefree", 3000.0, 30.0, 10.0, 60.0, LocalDate.now(), null);
        when(mockedDietPlanRepo.readActive()).thenReturn(mockedActiveDietplan);

        //mock all recipes in test db
        List<Recipe> allRecipes = getRecipes();
        allRecipes.forEach(NutritionUtil::fillNutritionValues);
        when(mockedRecipeRepo.getRecipes()).thenReturn(allRecipes);

        when(mockedMealRecommendationRepo.readRecommendationFor(mockedActiveDietplan, RecipeTag.B)).thenThrow(NoEntryFoundException.class);
        when(mockedMealRecommendationRepo.readRecommendationFor(mockedActiveDietplan, RecipeTag.L)).thenThrow(NoEntryFoundException.class);
        when(mockedMealRecommendationRepo.readRecommendationFor(mockedActiveDietplan, RecipeTag.D)).thenThrow(NoEntryFoundException.class);

        RecipeService recipeService = new SimpleRecipeService(mockedRecipeRepo);
        MealRecommendationsService mealRecommendationsService = new SimpleMealRecommendationsService(mockedMealRecommendationRepo, recipeService, mockedDietPlanRepo);
        try {
            Assert.assertFalse(mealRecommendationsService.getRecommendedMeals().isEmpty());
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test(expected = NoOptimalSolutionException.class)
    public void testGetRecommendedMeals_noOptimumBecauseNoRecipes_throwException() throws PersistenceException, NoEntryFoundException, ServiceInvokationException, NoOptimalSolutionException {
        DietPlan mockedActiveDietplan = new DietPlan(1, "Testplan", 2500.0, 20.0, 25.0, 50.0, LocalDate.now(), null);
        when(mockedDietPlanRepo.readActive()).thenReturn(mockedActiveDietplan);
        when(mockedMealRecommendationRepo.readRecommendationFor(mockedActiveDietplan, RecipeTag.B)).thenThrow(NoEntryFoundException.class);
        when(mockedMealRecommendationRepo.readRecommendationFor(mockedActiveDietplan, RecipeTag.L)).thenThrow(NoEntryFoundException.class);
        when(mockedMealRecommendationRepo.readRecommendationFor(mockedActiveDietplan, RecipeTag.D)).thenThrow(NoEntryFoundException.class);

        RecipeService recipeService = new SimpleRecipeService(mockedRecipeRepo);
        MealRecommendationsService mealRecommendationsService = new SimpleMealRecommendationsService(mockedMealRecommendationRepo, recipeService, mockedDietPlanRepo);
        mealRecommendationsService.getRecommendedMeals();
    }

    @Test(expected = NoOptimalSolutionException.class)
    public void testGetRecommendedMeals_noBreakfastRecipes_throwException() throws PersistenceException, NoEntryFoundException, ServiceInvokationException, NoOptimalSolutionException {
        //mock a plan where there cannot be optimal recipes
        DietPlan mockedActiveDietplan = new DietPlan(1, "Testplan", 2000.0, 60.0, 100.0, 150.0, LocalDate.now(), null);
        when(mockedDietPlanRepo.readActive()).thenReturn(mockedActiveDietplan);
        when(mockedMealRecommendationRepo.readRecommendationFor(mockedActiveDietplan, RecipeTag.B)).thenThrow(NoEntryFoundException.class);
        when(mockedMealRecommendationRepo.readRecommendationFor(mockedActiveDietplan, RecipeTag.L)).thenThrow(NoEntryFoundException.class);
        when(mockedMealRecommendationRepo.readRecommendationFor(mockedActiveDietplan, RecipeTag.D)).thenThrow(NoEntryFoundException.class);

        //mock all recipes in test db
        List<Recipe> allRecipes = getRecipes();

        //remove all recipes which are labeled as breakfast
        allRecipes.removeIf(recipe -> recipe.getTags().contains(RecipeTag.B));
        allRecipes.forEach(NutritionUtil::fillNutritionValues);
        when(mockedRecipeRepo.getRecipes()).thenReturn(allRecipes);

        RecipeService recipeService = new SimpleRecipeService(mockedRecipeRepo);
        MealRecommendationsService mealRecommendationsService = new SimpleMealRecommendationsService(mockedMealRecommendationRepo, recipeService, mockedDietPlanRepo);
        mealRecommendationsService.getRecommendedMeals();
    }

    @Test(expected = NoOptimalSolutionException.class)
    public void testGetRecommendedMeals_noLunchRecipes_throwException() throws PersistenceException, NoEntryFoundException, ServiceInvokationException, NoOptimalSolutionException {
        //mock a plan where there cannot be optimal recipes
        DietPlan mockedActiveDietplan = new DietPlan(1, "Testplan", 2000.0, 60.0, 100.0, 150.0, LocalDate.now(), null);
        when(mockedDietPlanRepo.readActive()).thenReturn(mockedActiveDietplan);
        when(mockedMealRecommendationRepo.readRecommendationFor(mockedActiveDietplan, RecipeTag.B)).thenThrow(NoEntryFoundException.class);
        when(mockedMealRecommendationRepo.readRecommendationFor(mockedActiveDietplan, RecipeTag.L)).thenThrow(NoEntryFoundException.class);
        when(mockedMealRecommendationRepo.readRecommendationFor(mockedActiveDietplan, RecipeTag.D)).thenThrow(NoEntryFoundException.class);

        //mock all recipes in test db
        List<Recipe> allRecipes = getRecipes();

        //remove all recipes which are labeled as lunch
        allRecipes.removeIf(recipe -> recipe.getTags().contains(RecipeTag.L));
        allRecipes.forEach(NutritionUtil::fillNutritionValues);
        when(mockedRecipeRepo.getRecipes()).thenReturn(allRecipes);

        RecipeService recipeService = new SimpleRecipeService(mockedRecipeRepo);
        MealRecommendationsService mealRecommendationsService = new SimpleMealRecommendationsService(mockedMealRecommendationRepo, recipeService, mockedDietPlanRepo);
        mealRecommendationsService.getRecommendedMeals();
    }

    @Test(expected = NoOptimalSolutionException.class)
    public void testGetRecommendedMeals_noDinnerRecipes_throwException() throws PersistenceException, NoEntryFoundException, ServiceInvokationException, NoOptimalSolutionException {
        //mock a plan where there cannot be optimal recipes
        DietPlan mockedActiveDietplan = new DietPlan(1, "Testplan", 2000.0, 60.0, 100.0, 150.0, LocalDate.now(), null);
        when(mockedDietPlanRepo.readActive()).thenReturn(mockedActiveDietplan);
        when(mockedMealRecommendationRepo.readRecommendationFor(mockedActiveDietplan, RecipeTag.B)).thenThrow(NoEntryFoundException.class);
        when(mockedMealRecommendationRepo.readRecommendationFor(mockedActiveDietplan, RecipeTag.L)).thenThrow(NoEntryFoundException.class);
        when(mockedMealRecommendationRepo.readRecommendationFor(mockedActiveDietplan, RecipeTag.D)).thenThrow(NoEntryFoundException.class);

        //mock all recipes in test db
        List<Recipe> allRecipes = getRecipes();

        //remove all recipes which are labeled as dinner
        allRecipes.removeIf(recipe -> recipe.getTags().contains(RecipeTag.D));
        allRecipes.forEach(NutritionUtil::fillNutritionValues);
        when(mockedRecipeRepo.getRecipes()).thenReturn(allRecipes);

        RecipeService recipeService = new SimpleRecipeService(mockedRecipeRepo);
        MealRecommendationsService mealRecommendationsService = new SimpleMealRecommendationsService(mockedMealRecommendationRepo, recipeService, mockedDietPlanRepo);
        mealRecommendationsService.getRecommendedMeals();
    }

    @Test(expected = NoEntryFoundException.class)
    public void testGetRecommendedMeals_noActivePlanSet_throwException() throws PersistenceException, NoEntryFoundException, ServiceInvokationException, NoOptimalSolutionException {
        //mock a plan where there cannot be optimal recipes
        DietPlan mockedActiveDietplan = new DietPlan(1, "Testplan", 2000.0, 60.0, 100.0, 150.0, LocalDate.now(), null);
        when(mockedDietPlanRepo.readActive()).thenThrow(NoEntryFoundException.class);

        //mock all recipes in test db
        List<Recipe> allRecipes = getRecipes();

        allRecipes.forEach(NutritionUtil::fillNutritionValues);
        when(mockedRecipeRepo.getRecipes()).thenReturn(allRecipes);

        RecipeService recipeService = new SimpleRecipeService(mockedRecipeRepo);
        MealRecommendationsService mealRecommendationsService = new SimpleMealRecommendationsService(mockedMealRecommendationRepo, recipeService, mockedDietPlanRepo);
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

    @Test
    public void testGetRecommendedMeals_persistenceHasRecommendations_getRecipesNotCalled() throws NoEntryFoundException, PersistenceException, NoOptimalSolutionException, ServiceInvokationException {
        DietPlan mockedActiveDietplan = new DietPlan(1, "Build Muscle", 2500.0, 20.0, 25.0, 50.0, LocalDate.now(), null);
        when(mockedDietPlanRepo.readActive()).thenReturn(mockedActiveDietplan);

        List<Recipe> allRecipes = getRecipes();
        // get recipes from example recipes which are a valid solution for diet plan 'Build Muscle'
        Recipe breakfast = NutritionUtil.fillNutritionValues(allRecipes.stream().filter(recipe -> recipe.getId() == 15).findFirst().get());
        Recipe lunch = NutritionUtil.fillNutritionValues(allRecipes.stream().filter(recipe -> recipe.getId() == 1).findFirst().get());
        Recipe dinner = NutritionUtil.fillNutritionValues(allRecipes.stream().filter(recipe -> recipe.getId() == 20).findFirst().get());

        when(mockedMealRecommendationRepo.readRecommendationFor(mockedActiveDietplan, RecipeTag.B)).thenReturn(breakfast);
        when(mockedMealRecommendationRepo.readRecommendationFor(mockedActiveDietplan, RecipeTag.L)).thenReturn(lunch);
        when(mockedMealRecommendationRepo.readRecommendationFor(mockedActiveDietplan, RecipeTag.D)).thenReturn(dinner);

        RecipeService recipeService = new SimpleRecipeService(mockedRecipeRepo);
        MealRecommendationsService mealRecommendationsService = new SimpleMealRecommendationsService(mockedMealRecommendationRepo, recipeService, mockedDietPlanRepo);
        mealRecommendationsService.getRecommendedMeals();

        verify(mockedRecipeRepo, times(0)).getRecipes();
    }

    @Test
    public void testGetRecommendedMeals_persistenceHasNoDinnerRecommendation_getRecipesCalledOnce() throws NoEntryFoundException, PersistenceException, NoOptimalSolutionException, ServiceInvokationException {
        DietPlan mockedActiveDietplan = new DietPlan(1, "Build Muscle", 2500.0, 20.0, 25.0, 50.0, LocalDate.now(), null);
        when(mockedDietPlanRepo.readActive()).thenReturn(mockedActiveDietplan);

        List<Recipe> allRecipes = getRecipes();
        allRecipes.forEach(NutritionUtil::fillNutritionValues);
        when(mockedRecipeRepo.getRecipes()).thenReturn(allRecipes);

        // get recipes from example recipes which are a valid solution for diet plan 'Build Muscle'
        Recipe breakfast = NutritionUtil.fillNutritionValues(allRecipes.stream().filter(recipe -> recipe.getId() == 15).findFirst().get());
        Recipe lunch = NutritionUtil.fillNutritionValues(allRecipes.stream().filter(recipe -> recipe.getId() == 1).findFirst().get());

        when(mockedMealRecommendationRepo.readRecommendationFor(mockedActiveDietplan, RecipeTag.B)).thenReturn(breakfast);
        when(mockedMealRecommendationRepo.readRecommendationFor(mockedActiveDietplan, RecipeTag.L)).thenReturn(lunch);
        when(mockedMealRecommendationRepo.readRecommendationFor(mockedActiveDietplan, RecipeTag.D)).thenThrow(NoEntryFoundException.class);

        RecipeService recipeService = new SimpleRecipeService(mockedRecipeRepo);
        MealRecommendationsService mealRecommendationsService = new SimpleMealRecommendationsService(mockedMealRecommendationRepo, recipeService, mockedDietPlanRepo);
        mealRecommendationsService.getRecommendedMeals();

        verify(mockedRecipeRepo, times(1)).getRecipes();
    }

    @Test
    public void testGetRecommendedMeals_persistenceHasOnlyLunchRecommendation_getRecipesCalledOnce() throws NoEntryFoundException, PersistenceException, NoOptimalSolutionException, ServiceInvokationException {
        DietPlan mockedActiveDietplan = new DietPlan(1, "Build Muscle", 2500.0, 20.0, 25.0, 50.0, LocalDate.now(), null);
        when(mockedDietPlanRepo.readActive()).thenReturn(mockedActiveDietplan);

        List<Recipe> allRecipes = getRecipes();
        allRecipes.forEach(NutritionUtil::fillNutritionValues);
        when(mockedRecipeRepo.getRecipes()).thenReturn(allRecipes);

        // get recipes from example recipes which are a valid solution for diet plan 'Build Muscle'
        Recipe lunch = NutritionUtil.fillNutritionValues(allRecipes.stream().filter(recipe -> recipe.getId() == 1).findFirst().get());

        when(mockedMealRecommendationRepo.readRecommendationFor(mockedActiveDietplan, RecipeTag.B)).thenThrow(NoEntryFoundException.class);
        when(mockedMealRecommendationRepo.readRecommendationFor(mockedActiveDietplan, RecipeTag.L)).thenReturn(lunch);
        when(mockedMealRecommendationRepo.readRecommendationFor(mockedActiveDietplan, RecipeTag.D)).thenThrow(NoEntryFoundException.class);

        RecipeService recipeService = new SimpleRecipeService(mockedRecipeRepo);
        MealRecommendationsService mealRecommendationsService = new SimpleMealRecommendationsService(mockedMealRecommendationRepo, recipeService, mockedDietPlanRepo);
        mealRecommendationsService.getRecommendedMeals();

        verify(mockedRecipeRepo, times(1)).getRecipes();
    }

    @Test(expected = NoOptimalSolutionException.class)
    public void testGetRecommendedMeal_allRecipesOmitted_throwException() throws PersistenceException, NoOptimalSolutionException, ServiceInvokationException {

        List<Recipe> allRecipes = getRecipes();
        allRecipes.forEach(NutritionUtil::fillNutritionValues);
        when(mockedRecipeRepo.getRecipes()).thenReturn(allRecipes);

        RecipeService recipeService = new SimpleRecipeService(mockedRecipeRepo);
        MealRecommendationsService mealRecommendationsService = new SimpleMealRecommendationsService(mockedMealRecommendationRepo, recipeService, mockedDietPlanRepo);
        mealRecommendationsService.getRecommendedMeal(RecipeTag.B, allRecipes);
    }

    @Test
    public void testGetRecommendedMeal_noRecipeOmitted_getRecipesCalledOnce() throws NoOptimalSolutionException, ServiceInvokationException, PersistenceException, NoEntryFoundException {
        DietPlan mockedActiveDietplan = new DietPlan(1, "Build Muscle", 2500.0, 20.0, 25.0, 50.0, LocalDate.now(), null);
        when(mockedDietPlanRepo.readActive()).thenReturn(mockedActiveDietplan);

        List<Recipe> allRecipes = getRecipes();
        allRecipes.forEach(NutritionUtil::fillNutritionValues);
        when(mockedRecipeRepo.getRecipes()).thenReturn(allRecipes);

        RecipeService recipeService = new SimpleRecipeService(mockedRecipeRepo);
        MealRecommendationsService mealRecommendationsService = new SimpleMealRecommendationsService(mockedMealRecommendationRepo, recipeService, mockedDietPlanRepo);
        mealRecommendationsService.getRecommendedMeal(RecipeTag.B, new ArrayList<Recipe>());

        verify(mockedRecipeRepo, times(1)).getRecipes();
    }
}
