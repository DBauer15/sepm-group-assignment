package at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import at.ac.tuwien.sepm.assignment.groupphase.application.service.ServiceInvokationContext;
import at.ac.tuwien.sepm.assignment.groupphase.application.ui.ExternalController;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

public class UserInterfaceUtility {

	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Autowired
	private static SpringFXMLLoader fxmlLoader;
	
	private UserInterfaceUtility() {}

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
		
	/**
	 * Open external controller
	 * 
	 * @param Path FXML file path
	 * @param Title window title
	 * @param object is the object that is viewed in the external controller (e.g. a Recipe)
	 * @param owner defines the window that has opened the external controller
	 * @param controller defines the class of the controller that should be opened (e.g. RecipeController)
	 */
	public static <T, TController extends ExternalController<T>> void loadExternalController(String Path, String Title,
			T object, Window owner, Class<TController> controller, SpringFXMLLoader fxmlLoader) {
		try {
			URL location = controller.getResource(Path);
			fxmlLoader.setLocation(location);
			Stage stage = new Stage();

			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(owner);
			stage.setTitle(Title);

			var load = fxmlLoader.loadAndWrap(controller.getResourceAsStream(Path), controller);
			load.getController().initializeView(object);
			stage.setScene(new Scene((Parent) load.getLoadedObject()));

			stage.showAndWait();
		} catch (IOException e) {
			LOG.error(e.getMessage());
		}
	}
}