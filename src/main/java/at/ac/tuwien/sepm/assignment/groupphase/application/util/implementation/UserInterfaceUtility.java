package at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation;

import javafx.scene.control.Alert;

public class UserInterfaceUtility {
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
}
