package at.ac.tuwien.sepm.assignment.groupphase.application.service.implementation;

import at.ac.tuwien.sepm.assignment.groupphase.application.dto.DietPlan;
import at.ac.tuwien.sepm.assignment.groupphase.application.dto.Recipe;
import at.ac.tuwien.sepm.assignment.groupphase.application.dto.RecipeTag;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.DietPlanPersistence;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.NoEntryFoundException;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.MealRecommendationsService;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.NoOptimalSolutionException;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.RecipeService;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.ServiceInvokationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.*;

@Service
public class SimpleMealRecommendationsService implements MealRecommendationsService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final RecipeService recipeService;
    private final DietPlanPersistence dietPlanPersistence;

    public SimpleMealRecommendationsService(RecipeService recipeService, DietPlanPersistence dietPlanPersistence) {
        this.recipeService = recipeService;
        this.dietPlanPersistence = dietPlanPersistence;
    }

    @Override
    public Map<RecipeTag, Recipe> getRecommendedMeals() throws ServiceInvokationException, NoOptimalSolutionException, NoEntryFoundException {
        LOG.debug("Requested recommended meals");

        Map<RecipeTag, Recipe> optimumMeals = new HashMap<>();
        List<Recipe> allRecipes;

        try {
            DietPlan currentDietPlan = dietPlanPersistence.readActive();
            allRecipes = recipeService.getRecipes();

            optimumMeals.put(RecipeTag.B, calculateOptimumForTag(currentDietPlan, allRecipes, RecipeTag.B));
            optimumMeals.put(RecipeTag.L, calculateOptimumForTag(currentDietPlan, allRecipes, RecipeTag.L));
            optimumMeals.put(RecipeTag.D, calculateOptimumForTag(currentDietPlan, allRecipes, RecipeTag.D));
        } catch (PersistenceException e) {
            throw new ServiceInvokationException(e.getMessage());
        }

        return optimumMeals;
    }

    private Recipe calculateOptimumForTag(DietPlan currentDietPlan, List<Recipe> allRecipes, RecipeTag tag) throws NoOptimalSolutionException {
        LOG.debug("Calculating Optimum for tag: " + tag);

        //threshold showing what recipe scores are acceptable
        double threshold = 2.5;
        Map<String, Double> limits = calculateLimits(currentDietPlan);
        List<Recipe> potentialRecipes = new ArrayList<>();

        for (Recipe r : allRecipes) {
            if (r.getTags().contains(tag)) {
                if (calculateScoreFor(r, limits) <= threshold) {
                    potentialRecipes.add(r);
                }
            }
        }

        //to prevent always returning the same recipes we randomly pick those that are good candidates
        if (potentialRecipes.size() > 0) {
            return potentialRecipes.get((int) Math.round(Math.random() * (potentialRecipes.size() - 1)));
        } else {
            throw new NoOptimalSolutionException("No optimal recipes found for tag " + tag);
        }
    }

    private Map<String, Double> calculateLimits(DietPlan dietPlan) {
        LOG.debug("Calculation limits for plan: " + dietPlan.toString());

        Map<String, Double> limits = new HashMap<>();

        limits.put("limitKcal", dietPlan.getEnergy_kcal()/3);
        limits.put("limitCarbohydrates", dietPlan.getCarbohydrate()/3);
        limits.put("limitProteins", dietPlan.getProtein()/3);
        limits.put("limitFats", dietPlan.getLipid()/3);

        return limits;
    }

    private double calculateScoreFor(Recipe r, Map<String, Double> limits) {
        LOG.debug("Calculating score for recipe: " + r.toString());

        //weights for final calculation between 0 and 1, less means less important
        double weightKcal = 1;
        double weightCarbohydrates = 1;
        double weightProteins = 1;
        double weightFats = 1;

        double scoreKcal = Math.abs((r.getCalories() / limits.get("limitKcal")) - 1);
        double scoreCarbohydrates = Math.abs((r.getCarbohydrates() / limits.get("limitCarbohydrates")) - 1);
        double scoreProteins = Math.abs((r.getProteins() / limits.get("limitProteins")) - 1);
        double scoreFats = Math.abs((r.getFats() / limits.get("limitFats")) - 1);

        //the closer the score is to 0, the better a recipes is
        double score = (scoreKcal * weightKcal) + (scoreCarbohydrates * weightCarbohydrates) + (scoreProteins * weightProteins) + (scoreFats * weightFats);

        LOG.debug("Calculated score was: " + score);
        return score;
    }
}
