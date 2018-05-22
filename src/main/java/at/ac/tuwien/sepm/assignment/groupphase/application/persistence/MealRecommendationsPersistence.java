package at.ac.tuwien.sepm.assignment.groupphase.application.persistence;

import at.ac.tuwien.sepm.assignment.groupphase.application.dto.DietPlan;
import at.ac.tuwien.sepm.assignment.groupphase.application.dto.Recipe;
import at.ac.tuwien.sepm.assignment.groupphase.application.dto.RecipeTag;

public interface MealRecommendationsPersistence {

    /**
     * Creates a new recommendation entry for a given {@link DietPlan}
     * @param recipe {@link Recipe} to save as recommendation
     * @param dietPlan {@link DietPlan} for which to save the recipe
     * @param recipeTag {@link RecipeTag} under which to save the given recipe
     * @throws PersistenceException
     */
    public void createRecommendationFor(Recipe recipe, DietPlan dietPlan, RecipeTag recipeTag) throws PersistenceException;


    /**
     * Reads the most recent recommendation for today for a given {@link DietPlan} and {@link RecipeTag}
     * @param dietPlan {@link DietPlan} for which to read the recommendation
     * @param recipeTag {@link RecipeTag} to read for the current day
     * @return {@link Recipe} that fits the plan and tag
     * @throws PersistenceException
     * @throws NoEntryFoundException Thrown when there is no according recommendation for today
     */
    public Recipe readRecommednationFor(DietPlan dietPlan, RecipeTag recipeTag) throws PersistenceException, NoEntryFoundException;
}
