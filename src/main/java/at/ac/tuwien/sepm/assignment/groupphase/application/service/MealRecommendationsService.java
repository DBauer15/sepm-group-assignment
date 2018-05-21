package at.ac.tuwien.sepm.assignment.groupphase.application.service;

import at.ac.tuwien.sepm.assignment.groupphase.application.dto.Recipe;
import at.ac.tuwien.sepm.assignment.groupphase.application.dto.RecipeTag;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.NoEntryFoundException;

import java.util.Map;


public interface MealRecommendationsService {

    /**
     * Calculates recipes for the day that best correspond with the current active plan.
     * @return A list of the recommended meals for the day in accordance with a plan
     */
    public Map<RecipeTag, Recipe> getRecommendedMeals() throws ServiceInvokationException, NoOptimalSolutionException, NoEntryFoundException;

}
