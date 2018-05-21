package at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation;

import java.util.Locale;

import org.apache.commons.validator.routines.DoubleValidator;

public class ValidationUtilUi {

	private static final Locale defaultLocale = Locale.US;

	private ValidationUtilUi() {}

	/**
	 * Validates the given value as Double with default Locale and
	 * if valid returns the parsed Double value, otherwise returns null.
	 * @param value {@link String}
	 * @return parsed Double value if valid, otherwise null
	 */
	public static Double validateAndGet(String value) {
		return DoubleValidator.getInstance().validate(value, defaultLocale);
	}
}
