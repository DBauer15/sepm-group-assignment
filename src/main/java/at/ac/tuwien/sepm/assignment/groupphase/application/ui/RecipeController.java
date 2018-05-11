package at.ac.tuwien.sepm.assignment.groupphase.application.ui;

import at.ac.tuwien.sepm.assignment.groupphase.application.dto.Recipe;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.RecipeService;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.ServiceInvokationException;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import java.lang.invoke.MethodHandles;

import static at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation.UserInterfaceUtility.showAlert;

@Controller
public class RecipeController {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @FXML
    TextField recipeNameTextField;

    @FXML
    TextArea directionsTextArea;

    @FXML
    Slider preparationTimeSlider;

    @FXML
    Button createButton;

    @FXML
    Label preparationTimeLabel;

    RecipeService recipeService;
    Recipe r;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
        r = new Recipe();
    }

    @FXML
    public void initialize() {
        preparationTimeSlider.valueProperty().addListener((ChangeListener) -> preparationTimeLabel.textProperty().setValue(
        String.valueOf((int) preparationTimeSlider.getValue()) + " minutes"));
    }

    @FXML
    public void onCreateButtonClicked(ActionEvent actionEvent) {
        LOG.info("Add recipe button clicked");

        r.setName(recipeNameTextField.getText());
        r.setDuration(preparationTimeSlider.getValue());
        r.setDescription(directionsTextArea.getText());


        try {
            recipeService.create(r);
            showAlert(Alert.AlertType.INFORMATION, "Saving successful.", "Recipe successfully saved.");
            LOG.debug("Recipe successfully saved.");
            ((Stage) createButton.getScene().getWindow()).close();
        } catch (ServiceInvokationException e) {
            showAlert(Alert.AlertType.WARNING, "Error.", e.getMessage());
            LOG.error(e.getMessage());
        }


    }
}
