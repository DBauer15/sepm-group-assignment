package at.ac.tuwien.sepm.assignment.groupphase.application.service;

import at.ac.tuwien.sepm.assignment.groupphase.application.dto.DietPlan;
import at.ac.tuwien.sepm.assignment.groupphase.application.dto.Recipe;

import java.util.List;

public interface MealRecommendationsService {

    /**
     * Calculates recipes for the day that are best for a given plan
     * @param dietPlan The currently set plan
     * @return A list of the recommended meals for the day in accordance with a plan
     */
    public List<Recipe> getRecommendedMeals(DietPlan dietPlan);

}
