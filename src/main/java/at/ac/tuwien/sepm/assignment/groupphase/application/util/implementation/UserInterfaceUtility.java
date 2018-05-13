package at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation;

import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.tuwien.sepm.assignment.groupphase.application.service.ServiceInvokationContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class UserInterfaceUtility {

	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	/**
	 * Method to show a "pop-up" Window containing information in the User interface
	 * @param alertType The alert type that the created window will have
	 * @param title The text that is shown as title on the window
	 * @param contentText The text that is shown in the pop-up window
	 */
	public static void showAlert(Alert.AlertType alertType, String title, String contentText) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(contentText);
		alert.showAndWait();
	}

	/**
	 * Append service invokation context error messages.
	 *
	 * @param context the context
	 * @return the string
	 */
	private static String append(ServiceInvokationContext context) {
		StringBuilder sb = new StringBuilder();
		for (String err : context.getErrors()) {
			sb.append(String.format("* %s\n", err));
		}
		return sb.toString();
	}

	/**
	 * Handle service invokation faults.
	 *
	 * @param context the context
	 */
	public static void handleFaults(ServiceInvokationContext context) {
		String contentText = append(context);
		LOG.debug("Errors occured: \r\n{}", contentText);
		showAlert(AlertType.ERROR, "An error occured.", contentText);
	}

	/**
	 * Handle exception fault.
	 *
	 * @param e the exception
	 */
	public static void handleFault(Exception e) {
		LOG.debug("Unexpected Errors occured: \r\n", e);
		showAlert(AlertType.ERROR, "An unexpected error occured.",
				String.format("An unexpected error occured :-( \nMessage: %s", e.getMessage()));
	}
}
