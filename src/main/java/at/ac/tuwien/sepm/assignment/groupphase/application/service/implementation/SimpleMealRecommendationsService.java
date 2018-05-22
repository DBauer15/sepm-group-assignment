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

    //threshold showing what recipe scores are acceptable
    private static final double THRESHOLD = 1.35;
    //fraction to use to split up total diet plan calories
    private static final double LIMIT_FRACTION = 1.0/4.0;
    //bias to be used for consecutive calculations
    private static double BIAS = 0;

    private final RecipeService recipeService;
    private final DietPlanPersistence dietPlanPersistence;

    public SimpleMealRecommendationsService(RecipeService recipeService, DietPlanPersistence dietPlanPersistence) {
        this.recipeService = recipeService;
        this.dietPlanPersistence = dietPlanPersistence;
    }

    @Override
    public Map<RecipeTag, Recipe> getRecommendedMeals() throws ServiceInvokationException, NoEntryFoundException, NoOptimalSolutionException {
        LOG.debug("Requested recommended meals");

        Map<RecipeTag, Recipe> optimumMeals = new HashMap<>();
        List<Recipe> allRecipes;

        try {
            DietPlan currentDietPlan = dietPlanPersistence.readActive();
            allRecipes = recipeService.getRecipes();

            BIAS = 0;
            optimumMeals.put(RecipeTag.B, calculateOptimumForTag(currentDietPlan, allRecipes, RecipeTag.B,1));
            optimumMeals.put(RecipeTag.L, calculateOptimumForTag(currentDietPlan, allRecipes, RecipeTag.L,2));
            optimumMeals.put(RecipeTag.D, calculateOptimumForTag(currentDietPlan, allRecipes, RecipeTag.D,1));
        } catch (PersistenceException e) {
            throw new ServiceInvokationException(e.getMessage());
        }

        return optimumMeals;
    }

    private Recipe calculateOptimumForTag(DietPlan currentDietPlan, List<Recipe> allRecipes, RecipeTag tag, double fractionFactor) throws NoOptimalSolutionException {
        LOG.debug("Calculating Optimum for tag: " + tag);

        Map<Recipe, Double> scoredRecipes = new HashMap<>();
        Map<Recipe, Double> potentialRecipes = new HashMap<>();

        for (Recipe r : allRecipes) {
            if (r.getTags().contains(tag)) {
                double score = calculateScoreFor(currentDietPlan, r, fractionFactor);
                scoredRecipes.put(r, score);
                if (score <= THRESHOLD + BIAS) {
                    potentialRecipes.put(r, score);
                }
            }
        }

        if (scoredRecipes.keySet().size() <= 0) {
            throw new NoOptimalSolutionException("No " + tag.toString() + " recipes in the cookbook found. Cannot generate recommendations.");
        }

        //to prevent always returning the same recipes we randomly pick those that are good candidates
        if (potentialRecipes.size() > 0) {
            Recipe response = potentialRecipes.keySet().toArray(new Recipe[potentialRecipes.keySet().size()])
                [(int) Math.round(Math.random() * (potentialRecipes.size() - 1))];
            //Calculate a bias based on how good or poor the current choice was.
            //Bias is based on half the bias+threshold value for midpoint reference
            //New bias is based on how good or bad the chosen recipe performed with the current bias
            BIAS = ((THRESHOLD+BIAS)/2) - potentialRecipes.get(response);
            return response;
        } else {
            //If there was no 'optimal' recipe, we return the next best one
            double min = -1;
            Recipe minRecipe = null;
            for (Recipe r : scoredRecipes.keySet()) {
                if (minRecipe == null) {
                    minRecipe = r;
                    min = scoredRecipes.get(r);
                } else if (scoredRecipes.get(r) < min) {
                    minRecipe = r;
                    min = scoredRecipes.get(r);
                }
            }
            return minRecipe;
        }
    }

    private double calculateScoreFor(DietPlan p, Recipe r, double fractionFactor) {
        LOG.debug("Calculating score for recipe: " + r.toString());

        //weights for final calculation between 0 and 1, less means less important
        double weightKcal = 1;
        double weightCarbohydrates = 1;
        double weightProteins = 1;
        double weightFats = 1;

        double scoreKcal = Math.abs((r.getCalories() / (p.getEnergy_kcal()*LIMIT_FRACTION*fractionFactor)) - 1);
        double scoreCarbohydrates = Math.abs((r.getCarbohydratePercent() / p.getCarbohydrate()) - 1);
        double scoreProteins = Math.abs((r.getProteinPercent() / p.getProtein()) - 1);
        double scoreFats = Math.abs((r.getFatPercent() / p.getLipid()) - 1);

        //the closer the score is to 0, the better a recipes is
        double score = (scoreKcal * weightKcal) + (scoreCarbohydrates * weightCarbohydrates) + (scoreProteins * weightProteins) + (scoreFats * weightFats);

        LOG.debug("Calculated score was: " + score);
        return score;
    }
}
