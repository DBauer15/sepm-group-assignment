package at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation;

import at.ac.tuwien.sepm.assignment.groupphase.application.dto.Recipe;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.ServiceInvokationContext;
import at.ac.tuwien.sepm.assignment.groupphase.application.util.Validator;

public class RecipeValidator implements Validator<Recipe> {

	@Override
	public boolean validateForCreation(Recipe recipe, ServiceInvokationContext context) {

		ValidationUtil.validateStringLength("Recipe name", recipe.getName(), 0, 255, context);
		ValidationUtil.validateDoubleLimits("Duration", recipe.getDuration(), 0d, 255d, context);
		ValidationUtil.validateStringLength("Description", recipe.getDescription(), 1, null, context);
		ValidationUtil.validateNull("Ingredient Selection", recipe.getRecipeIngredients(), context);


		if (recipe.getTags() == null || recipe.getTags().size() == 0) {
			context.addError(String.format("Select at least one tag (breakfast, lunch or dinner)"));
		}
		
		if (recipe.getRecipeIngredients() != null && recipe.getRecipeIngredients().size() == 0) {
			context.addError("Select at least one ingredient for the recipe.");
		}
		return context.isValid();
	}

	@Override
	public boolean validateForReading(Recipe recipe, ServiceInvokationContext context) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean validateForUpdate(Recipe recipe, ServiceInvokationContext context) {
		// TODO Auto-generated method stub
		return false;
	}

}
