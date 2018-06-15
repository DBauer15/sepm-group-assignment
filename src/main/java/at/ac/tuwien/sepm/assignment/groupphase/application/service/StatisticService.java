package at.ac.tuwien.sepm.assignment.groupphase.application.service;

import at.ac.tuwien.sepm.assignment.groupphase.application.dto.Recipe;

import java.util.Map;

public interface StatisticService {
    /**
     * Fetches the most popular recipes with their according quantity
     * @return A list of the most popular recipes and their according quantity
     */
     Map<Recipe, Integer> getMostPopularRecipes() throws ServiceInvokationException;

}
