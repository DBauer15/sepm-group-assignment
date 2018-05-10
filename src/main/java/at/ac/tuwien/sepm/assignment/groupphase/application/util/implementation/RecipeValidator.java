package at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation;

import at.ac.tuwien.sepm.assignment.groupphase.application.dto.Recipe;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.ServiceInvokationContext;
import at.ac.tuwien.sepm.assignment.groupphase.application.util.Validator;

public class RecipeValidator implements Validator<Recipe> {

	@Override
	public boolean validateForCreation(Recipe recipe, ServiceInvokationContext context) {
		ValidationUtil.validateRecipe(recipe, context);
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
