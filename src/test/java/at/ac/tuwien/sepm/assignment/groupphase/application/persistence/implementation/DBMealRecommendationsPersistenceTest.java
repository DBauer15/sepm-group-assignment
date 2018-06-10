package at.ac.tuwien.sepm.assignment.groupphase.application.persistence.implementation;

import java.time.LocalDate;
import java.util.EnumSet;

import org.junit.Assert;
import org.junit.Test;

import at.ac.tuwien.sepm.assignment.groupphase.application.dto.DietPlan;
import at.ac.tuwien.sepm.assignment.groupphase.application.dto.Recipe;
import at.ac.tuwien.sepm.assignment.groupphase.application.dto.RecipeTag;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.MealRecommendationsPersistence;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.NoEntryFoundException;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.RecipePersistence;
import at.ac.tuwien.sepm.assignment.groupphase.application.util.BaseTest;

public class DBMealRecommendationsPersistenceTest extends BaseTest {

    RecipePersistence recipePersistence = new DBRecipePersistence();
    MealRecommendationsPersistence mealRecommendationsPersistence = new DBMealRecommendationsPersistence(recipePersistence);

    DietPlan dietPlan = new DietPlan(1, "Build Muscle", 2500.0, 25.0, 25.0, 50.0, LocalDate.now(), null);
    Recipe breakfastRecipe = new Recipe(1, "My recipe", 120d, "Test", EnumSet.of(RecipeTag.B), false);

    @Test
    public void testCreateRecommendationFor_withValidData_successWithRecipeCreated() throws PersistenceException {
        mealRecommendationsPersistence.createRecommendationFor(breakfastRecipe, dietPlan, RecipeTag.B);
    }

    @Test
    public void testReadRecommendationFor_withValidData_successReading() throws PersistenceException, NoEntryFoundException {
        Recipe nonStandardIDRecipe = new Recipe(3, "Random recipe", 120d, "Test", EnumSet.of(RecipeTag.B), false);

        mealRecommendationsPersistence.createRecommendationFor(nonStandardIDRecipe, dietPlan, RecipeTag.B);

        Recipe readRecipe = mealRecommendationsPersistence.readRecommendationFor(dietPlan, RecipeTag.B);

        Assert.assertEquals(nonStandardIDRecipe.getId(), readRecipe.getId());
    }

    @Test
    public void testReadRecommendationFor_withMoreRecentEntry_successReadingMostRecent() throws PersistenceException, NoEntryFoundException {
        Recipe nonStandardIDRecipe = new Recipe(3, "Random recipe", 120d, "Test", EnumSet.of(RecipeTag.B), false);

        mealRecommendationsPersistence.createRecommendationFor(breakfastRecipe, dietPlan, RecipeTag.B);
        // wait a second after insert again
        try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        mealRecommendationsPersistence.createRecommendationFor(nonStandardIDRecipe, dietPlan, RecipeTag.B);

        Recipe readRecipe = mealRecommendationsPersistence.readRecommendationFor(dietPlan, RecipeTag.B);

        Assert.assertEquals(nonStandardIDRecipe.getId(), readRecipe.getId());
    }

    @Test (expected = NoEntryFoundException.class)
    public void testReadRecommendationFor_withoutValidData_throwsNoEntryFoundExcpetion() throws NoEntryFoundException, PersistenceException {
        mealRecommendationsPersistence.readRecommendationFor(dietPlan, RecipeTag.B);
    }
}
