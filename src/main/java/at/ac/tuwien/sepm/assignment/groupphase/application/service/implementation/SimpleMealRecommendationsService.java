package at.ac.tuwien.sepm.assignment.groupphase.application.service.implementation;

import at.ac.tuwien.sepm.assignment.groupphase.application.dto.DietPlan;
import at.ac.tuwien.sepm.assignment.groupphase.application.dto.Recipe;
import at.ac.tuwien.sepm.assignment.groupphase.application.dto.RecipeTag;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.DietPlanPersistence;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.MealRecommendationsPersistence;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.NoEntryFoundException;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.MealRecommendationsService;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.NoOptimalSolutionException;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.RecipeService;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.ServiceInvokationException;
import at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation.NutritionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.*;

@Service
public class SimpleMealRecommendationsService implements MealRecommendationsService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    //threshold showing what recipe scores are acceptable
    private static final double THRESHOLD = 0.6;
    //fraction to use to split up total diet plan calories
    private static final double LIMIT_FRACTION = 1.0/4.0;
    //bias to be used for consecutive calculations
    private static double BIAS = 0;
    //fraction factors for kcal score
    private static final double[] FRACTION_FACTORS = new double[]{1, 2, 1};

    private final MealRecommendationsPersistence mealRecommendationsPersistence;
    private final RecipeService recipeService;
    private final DietPlanPersistence dietPlanPersistence;

    public SimpleMealRecommendationsService(MealRecommendationsPersistence mealRecommendationsPersistence, RecipeService recipeService, DietPlanPersistence dietPlanPersistence) {
        this.mealRecommendationsPersistence = mealRecommendationsPersistence;
        this.recipeService = recipeService;
        this.dietPlanPersistence = dietPlanPersistence;
    }

    @Override
    public Map<RecipeTag, Recipe> getRecommendedMeals() throws ServiceInvokationException, NoEntryFoundException, NoOptimalSolutionException {
        LOG.debug("Requested recommended meals");

        Map<RecipeTag, Recipe> optimumMeals = new HashMap<>();
        List<Recipe> allRecipes = null;

        try {
            DietPlan currentDietPlan = dietPlanPersistence.readActive();
            BIAS = 0;

            RecipeTag[] values = {RecipeTag.B, RecipeTag.L, RecipeTag.D};
            for (int i = 0; i < FRACTION_FACTORS.length; i++) {
                RecipeTag tag = values[i];
                try {
                    optimumMeals.put(tag, NutritionUtil.fillNutritionValues(mealRecommendationsPersistence.readRecommendationFor(currentDietPlan, tag)));
                } catch (NoEntryFoundException e) {
                    if (allRecipes == null)
                        allRecipes = recipeService.getRecipes();
                    Recipe r = calculateOptimumForTag(currentDietPlan, allRecipes, tag, FRACTION_FACTORS[i]);
                    mealRecommendationsPersistence.createRecommendationFor(r, currentDietPlan, tag);
                    optimumMeals.put(tag, r);
                }
            }
        } catch (PersistenceException e) {
            throw new ServiceInvokationException(e.getMessage());
        }

        return optimumMeals;
    }

    @Override
    public Recipe getRecommendedMeal(RecipeTag meal, List<Recipe> omissions) throws ServiceInvokationException, NoOptimalSolutionException {
        LOG.debug("Requested recommended meal for tag {}", meal);

        try {
            BIAS = 0;
            List<Recipe> allRecipes = recipeService.getRecipes();
            allRecipes.removeAll(omissions);
            DietPlan currentDietPlan = dietPlanPersistence.readActive();
            List<RecipeTag> tags = new ArrayList<>(Arrays.asList(RecipeTag.B, RecipeTag.L, RecipeTag.D));

            Recipe recipe = calculateOptimumForTag(currentDietPlan, allRecipes, meal, FRACTION_FACTORS[tags.indexOf(meal)]);
            mealRecommendationsPersistence.createRecommendationFor(recipe, currentDietPlan, meal);
            return recipe;
        } catch (PersistenceException e) {
            throw new ServiceInvokationException(e.getMessage());
        } catch (NoEntryFoundException e) {
            throw new ServiceInvokationException(e.getMessage());
        }
    }

    private Recipe calculateOptimumForTag(DietPlan currentDietPlan, List<Recipe> allRecipes, RecipeTag tag, double fractionFactor) throws NoOptimalSolutionException {
        LOG.debug("Calculating Optimum for tag: {}", tag);

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
            throw new NoOptimalSolutionException("No recipes tagged for meal " + tag + " found in the cookbook. Cannot generate recommendations.");
        }

        //to prevent always returning the same recipes we randomly pick those that are good candidates
        if (potentialRecipes.size() > 0) {
            int index = (int) Math.round(Math.random() * (potentialRecipes.size() - 1));
            Recipe response = potentialRecipes.keySet().toArray(new Recipe[potentialRecipes.keySet().size()])
                [index];
            //Calculate a bias based on how good or poor the current choice was.
            //Bias is based on half the bias+threshold value for midpoint reference
            //New bias is based on how good or bad the chosen recipe performed with the current bias
            BIAS = ((THRESHOLD+BIAS)/2) - potentialRecipes.get(response);
            LOG.debug("Selecting from {} potential recipes...", potentialRecipes.size());
            LOG.debug("Selected recipe with score: {}", potentialRecipes.get(response));
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
            LOG.debug("Selecting best out of {} recipes...", scoredRecipes.size());
            LOG.debug("Selected recipe with score: {}", min);
            return minRecipe;
        }
    }

    private double calculateScoreFor(DietPlan p, Recipe r, double fractionFactor) {
        LOG.trace("Calculating score for recipe: {}", r.toString());

        //weights for final calculation between 0 and 1, less means less important
        double weightKcal = 10 / 4;
        double weightCarbohydrates = 2 / 4;
        double weightProteins = 2 / 4;
        double weightFats = 2 / 4;

        double scoreKcal = Math.abs((r.getCalories() / (p.getEnergy_kcal()*LIMIT_FRACTION*fractionFactor)) - 1);
        double scoreCarbohydrates = Math.abs((r.getCarbohydratePercent() / p.getCarbohydrate()) - 1);
        double scoreProteins = Math.abs((r.getProteinPercent() / p.getProtein()) - 1);
        double scoreFats = Math.abs((r.getFatPercent() / p.getLipid()) - 1);

        //the closer the score is to 0, the better a recipes is
        double score = (scoreKcal * weightKcal) + (scoreCarbohydrates * weightCarbohydrates) + (scoreProteins * weightProteins) + (scoreFats * weightFats);

        LOG.trace("Calculated score was: {}", score);
        return score;
    }
}
