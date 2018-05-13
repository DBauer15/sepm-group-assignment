package at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation;

import at.ac.tuwien.sepm.assignment.groupphase.application.dto.Recipe;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.ServiceInvokationContext;
import at.ac.tuwien.sepm.assignment.groupphase.application.util.Validator;

public class RecipeValidator implements Validator<Recipe> {

	private final RecipeIngredientsValidator recipeIngredientsValidator = new RecipeIngredientsValidator();

	@Override
	public boolean validateForCreation(Recipe recipe, ServiceInvokationContext context) {

		ValidationUtil.validateStringLength("Recipe name", recipe.getName().trim(), 1, 255, context);
		ValidationUtil.validateDoubleLimits("Duration", recipe.getDuration(), 0d, 255d, context);
		ValidationUtil.validateStringLength("Description", recipe.getDescription().trim(), 1, null, context);

		if (recipe.getTags() == null || recipe.getTags().size() == 0) {
			context.addError(String.format("Select at least one tag (breakfast, lunch or dinner)"));
		}
		recipeIngredientsValidator.validateForCreation(recipe.getRecipeIngredients(), context);

		return context.isValid();
	}

	@Override
	public boolean validateForReading(Recipe recipe, ServiceInvokationContext context) {
		return false;
	}

	@Override
	public boolean validateForUpdate(Recipe recipe, ServiceInvokationContext context) {
		ValidationUtil.validateId(recipe.getId(), context);
		validateForCreation(recipe, context);
		return context.isValid();
	}
}
