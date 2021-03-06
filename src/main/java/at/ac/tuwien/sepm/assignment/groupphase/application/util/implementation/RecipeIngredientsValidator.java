package at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation;

import java.util.List;

import at.ac.tuwien.sepm.assignment.groupphase.application.dto.RecipeIngredient;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.ServiceInvokationContext;
import at.ac.tuwien.sepm.assignment.groupphase.application.util.Validator;
import org.springframework.stereotype.Component;

@Component
public class RecipeIngredientsValidator implements Validator<List<RecipeIngredient>> {

	@Override
	public boolean validateForCreation(List<RecipeIngredient> dto, ServiceInvokationContext context) {
		if (ValidationUtil.validateNull("Ingredient Selection", dto, context) == true) {
			return false;
		}
		if (dto.size() == 0) {
			context.addError("Select at least one ingredient for the recipe.");
		}

		for (RecipeIngredient ri : dto) {

			// amount must be > 0
			ValidationUtil.validateDoubleLimits(
					String.format("Amount of ingredient %s",
							ri.getIngredientName() == null ? "" : ri.getIngredientName().trim()),
					ri.getAmount(), 0d, null, context);

			if (ri.getId() != null && ri.getId() > 0) {
				continue; // a common ingredient
			}

			// trimmed ingredient name >= 1 chars
			ValidationUtil.validateStringLength("Ingredient Name", ri.getIngredientName().trim(), 1, 255, context);

			// unit gram > 0
			ValidationUtil.validateDoubleLimits(String.format("Unit gram of ingredient %s", ri.getIngredientName()),
					ri.getUnitGramNormalised(), 0d, null, context);

			// unit name >= 1 chars
			ValidationUtil.validateStringLength(String.format("Unit name of ingredient %s", ri.getIngredientName()),
					ri.getUnitName().trim(), 1, 255, context);

			// energy must be >= 0
			ValidationUtil.validateDoubleLimitsInclusive(
					String.format("Energy (kcal) of ingredient %s", ri.getIngredientName()), ri.getEnergyKcal(), 0d,
					null, context);
			// nutrients must be >= 0 and <= 100
			ValidationUtil.validateDoubleLimitsInclusive(
					String.format("Carbohydrate of ingredient %s", ri.getIngredientName()), ri.getCarbohydrate(), 0d,
					100d, context);
			ValidationUtil.validateDoubleLimitsInclusive(
					String.format("Lipid of ingredient %s", ri.getIngredientName()), ri.getLipid(), 0d, 100d, context);
			ValidationUtil.validateDoubleLimitsInclusive(
					String.format("Protein of ingredient %s", ri.getIngredientName()), ri.getProtein(), 0d, 100d,
					context);

			// sum of carbohydrate, lipid, protein must be <= 100
			final double nutrientSumPer100g = ri.getCarbohydrate() + ri.getLipid() + ri.getProtein();
			ValidationUtil.validateDoubleLimitsInclusive(
					String.format("Sum of nutrient/100g for ingredient %s", ri.getIngredientName()), nutrientSumPer100g,
					null, 100d, context);
		}

		// check if ingredient id exists multiple times, to avoid key constraint error
		// in recipient_ingredent table
		boolean foundDuplicateIngredientId = false;
		for (RecipeIngredient ri1 : dto) {
			for (RecipeIngredient ri2 : dto) {
				if (ri1.getId() != null && ri2.getId() != null && ri1.getId().intValue() == ri2.getId().intValue()
						&& !ri1.equals(ri2)) {
					foundDuplicateIngredientId = true;
				}
			}
		}
		if (foundDuplicateIngredientId == true) {
			context.addError("The same ingredient cannot be added more than once.");
		}

		return context.isValid();
	}

	@Override
	public boolean validateForReading(List<RecipeIngredient> dto, ServiceInvokationContext context) {
        if (validateForCreation(dto, context))
            for (RecipeIngredient ri : dto) {
                ValidationUtil.validateId(ri.getId(), context);
                ValidationUtil.validateNull("userSpecific", ri.getUserSpecific(), context);
            }

        return context.isValid();
	}

	@Override
	public boolean validateForUpdate(List<RecipeIngredient> dto, ServiceInvokationContext context) {
	    if (validateForCreation(dto, context))
            for (RecipeIngredient ri : dto)
                if (!ri.getUserSpecific())
                    ValidationUtil.validateId(ri.getId(), context);

        return context.isValid();
	}

}
