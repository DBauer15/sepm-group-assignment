package at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation;

import at.ac.tuwien.sepm.assignment.groupphase.application.service.ServiceInvokationContext;

public class ValidationUtil {

	public static void validateStringLength(String fieldName, String value, Integer minLength, Integer maxLength,
			ServiceInvokationContext context) {
		if (maxLength != null && value != null && value.length() > maxLength) {
			context.addError(String.format("Enter only %s characters in the field '%s'", maxLength, fieldName));
		}
		if (minLength != null && value != null && value.length() < minLength) {
			context.addError(String.format("Enter at least %s characters in the field '%s'", minLength, fieldName));
		}
	}

	public static void validateDoubleLimits(String fieldName, Double value, Double lowerBound, Double upperBound,
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

	public static void validateId(Integer id, ServiceInvokationContext context) {
        if (id == null) {
            context.addError("ID needs to be set to perform update");
        }
        if (id != null && id < 0) {
            context.addError("ID cannot be negative");
        }
    }
}
