package at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation;

import at.ac.tuwien.sepm.assignment.groupphase.application.dto.Recipe;
import at.ac.tuwien.sepm.assignment.groupphase.application.dto.RecipeIngredient;

public class NutritionUtil {

    private NutritionUtil() {}

    public static Recipe fillNutritionValues(Recipe r){

        double ingredientWeight;
        double totalCalories = 0;
        double totalProteins = 0;
        double totalCarbs = 0;
        double totalFats = 0;

        for (RecipeIngredient ingredient: r.getRecipeIngredients()
             ) {
            ingredientWeight = ingredient.getAmount()*ingredient.getUnitGramNormalised();

            totalCalories += ingredientWeight *ingredient.getEnergyKcal()/100;
            totalProteins += ingredientWeight * ingredient.getProtein()/100;
            totalCarbs += ingredientWeight *ingredient.getCarbohydrate()/100;
            totalFats += ingredientWeight *ingredient.getLipid()/100;
        }

        r.setCalories(totalCalories);
        r.setCarbohydrates(totalCarbs);
        r.setProteins(totalProteins);
        r.setFats(totalFats);

        return r;
    }
}
