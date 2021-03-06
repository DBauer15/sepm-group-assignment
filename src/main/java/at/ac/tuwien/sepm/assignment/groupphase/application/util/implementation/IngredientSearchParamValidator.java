package at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation;

import at.ac.tuwien.sepm.assignment.groupphase.application.dto.IngredientSearchParam;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.ServiceInvokationContext;
import at.ac.tuwien.sepm.assignment.groupphase.application.util.Validator;
import org.springframework.stereotype.Component;

/**
 * IngredientSearchParamValidator
 * @author e01529136
 *
 */

@Component
public class IngredientSearchParamValidator implements Validator<IngredientSearchParam> {

	@Override
	public boolean validateForCreation(IngredientSearchParam dto, ServiceInvokationContext context) {
		return false;
	}

	@Override
	public boolean validateForReading(IngredientSearchParam dto, ServiceInvokationContext context) {
		if (ValidationUtil.validateNull("Ingredient Search Param", dto, context))
		    return false;
		if (ValidationUtil.validateNull("Ingredient Name", dto.getIngredientName(), context))
		    return false;
		ValidationUtil.validateStringLength("Ingredient Name", dto.getIngredientName().trim(), 3, 255, context);
		return context.isValid();
	}

	@Override
	public boolean validateForUpdate(IngredientSearchParam dto, ServiceInvokationContext context) {
		return false;
	}

}
