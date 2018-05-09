package at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation;

import at.ac.tuwien.sepm.assignment.groupphase.application.dto.DietPlan;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.ServiceInvokationContext;

public class ValidationUtil {

    public static boolean validateDietPlanForUpdate(DietPlan dietPlan, ServiceInvokationContext context) {
        ValidationUtil.validateId(dietPlan.getId(), context);
        validateDietPlanForCreation(dietPlan, context);

        return context.isValid();
    }

	public static boolean validateDietPlanForCreation(DietPlan dietPlan, ServiceInvokationContext context) {
		ValidationUtil.validateStringLength("Diet plan name", dietPlan.getName(), 5, 255, context);

		// TODO was sind sinnvolle schranken?
		ValidationUtil.validateDoubleLimits("Energy Kcal", dietPlan.getEnergy_kcal(), 0d, 50d, context);
		ValidationUtil.validateDoubleLimits("Lipid", dietPlan.getLipid(), 0d, 50d, context);
		ValidationUtil.validateDoubleLimits("Protein", dietPlan.getProtein(), 0d, 50d, context);
		ValidationUtil.validateDoubleLimits("Carbohydrate", dietPlan.getCarbohydrate(), 0d, 50d, context);
		return context.isValid();
	}

	private static void validateStringLength(String fieldName, String value, Integer minLength, Integer maxLength,
			ServiceInvokationContext context) {
		if (maxLength != null && value != null && value.length() > maxLength) {
			context.addError(String.format("Enter only %s characters in the field '%s'", maxLength, fieldName));
		}
		if (minLength != null && value != null && value.length() < minLength) {
			context.addError(String.format("Enter at least %s characters in the field '%s'", minLength, fieldName));
		}
	}

	private static void validateDoubleLimits(String fieldName, Double value, Double lowerBound, Double upperBound,
			ServiceInvokationContext context) {
		if (lowerBound != null && value <= lowerBound) {
			context.addError(
					String.format("Enter a value that is greater than %s in the field '%s'", lowerBound, fieldName));
		}
		if (upperBound != null && value >= upperBound) {
			context.addError(
					String.format("Enter a value that is smaller than %s in the field '%s'", upperBound, fieldName));
		}
	}

	private static void validateId(Integer id, ServiceInvokationContext context) {
        if (id == null) {
            context.addError("ID needs to be set to perform update");
        }
        if (id != null && id < 0) {
            context.addError("ID cannot be negative");
        }
    }
}
