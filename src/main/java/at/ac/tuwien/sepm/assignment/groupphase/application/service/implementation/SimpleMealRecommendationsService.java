package at.ac.tuwien.sepm.assignment.groupphase.application.service.implementation;

import at.ac.tuwien.sepm.assignment.groupphase.application.dto.DietPlan;
import at.ac.tuwien.sepm.assignment.groupphase.application.dto.Recipe;
import at.ac.tuwien.sepm.assignment.groupphase.application.dto.RecipeTag;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.DietPlanPersistence;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.NoEntryFoundException;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.RecipePersistence;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.MealRecommendationsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.*;

@Service
public class SimpleMealRecommendationsService implements MealRecommendationsService{

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final RecipePersistence recipePersistence;
    private final DietPlanPersistence dietPlanPersistence;

    public SimpleMealRecommendationsService(RecipePersistence recipePersistence, DietPlanPersistence dietPlanPersistence) {
        this.recipePersistence = recipePersistence;
        this.dietPlanPersistence = dietPlanPersistence;
    }

    @Override
    public Map<RecipeTag, Recipe> getRecommendedMeals() {
        Map<RecipeTag, Recipe> optimumMeals = new HashMap<>();
        List<Recipe> allRecipes;

        try {
            DietPlan currentDietPlan = dietPlanPersistence.readActive();
            allRecipes = recipePersistence.getRecipes();

            optimumMeals.put(RecipeTag.B, calculateOptimumForTag(currentDietPlan, allRecipes, RecipeTag.B));
            optimumMeals.put(RecipeTag.L, calculateOptimumForTag(currentDietPlan, allRecipes, RecipeTag.L));
            optimumMeals.put(RecipeTag.D, calculateOptimumForTag(currentDietPlan, allRecipes, RecipeTag.D));

        } catch (PersistenceException e) {
            e.printStackTrace();
        } catch (NoEntryFoundException e) {
            e.printStackTrace();
        }

        return optimumMeals;
    }

    private Recipe calculateOptimumForTag(DietPlan currentDietPlan, List<Recipe> allRecipes, RecipeTag tag) throws NoEntryFoundException {
        double threshold = 0.5;
        Map limits = calculateLimits(currentDietPlan);
        List<Recipe> potentialRecipes = new ArrayList<>();

        for(Recipe r : allRecipes){
            if(r.getTags().contains(tag)) {
                if(calculateScoreFor(r, limits) <= threshold) {
                    potentialRecipes.add(r);
                }
            }
        }

        if(potentialRecipes.size() > 0){
            return potentialRecipes.get((int) Math.round(Math.random() * (potentialRecipes.size() - 1)));
        } else {
            throw new NoEntryFoundException("No optimal recipes found for tag " + tag);
        }
    }

    private Map calculateLimits(DietPlan dietPlan) {
        Map<String, double> limits = new HashMap();

        limits.put("limitKcal", dietPlan.getEnergy_kcal() / 4);
        limits.put("limitCarbohydrates", dietPlan.getCarbohydrate() / 4);
        limits.put("limitProteins", dietPlan.getProtein() / 4);
        limits.put("limitFats", dietPlan.getLipid() / 4);

        return limits;
    }

    private double calculateScoreFor(Recipe r, Map<String, double> limits) {
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

        return score;
    }
}
