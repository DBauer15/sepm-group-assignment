package at.ac.tuwien.sepm.assignment.groupphase.application.service;

import java.util.ArrayList;

/**
 * Holds errors from service invokation
 * @author e01529136
 *
 */
public class ServiceInvokationContext {

	private ArrayList<String> errors = new ArrayList<String>();

	/**
	 * Get List of Error message occurred on service invokation
	 * @return {@link String} list
	 */
	public ArrayList<String> getErrors() {
		return errors;
	}

	/**
	 * Add a message to service invokation error messages
	 * @param message {@link String} error message
	 */
	public void addError(String message) {
		this.errors.add(message);
	}

	public boolean isValid() {
		return getErrors().size() == 0;
	}

}
