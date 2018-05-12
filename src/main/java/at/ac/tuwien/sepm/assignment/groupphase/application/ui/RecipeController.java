package at.ac.tuwien.sepm.assignment.groupphase.application.ui;

import at.ac.tuwien.sepm.assignment.groupphase.application.dto.Recipe;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.RecipeService;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.ServiceInvokationException;
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
    Label headerLabel;

    @FXML
    TextField recipeNameTextField;

    @FXML
    TextArea directionsTextArea;

    @FXML
    Slider preparationTimeSlider;

    @FXML
    Button saveButton;

    @FXML
    Label preparationTimeLabel;

    private RecipeService recipeService;
    private Recipe r;
    private boolean isInEditMode = false;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    void initializeView(Recipe r) {
        if (r != null) {
            isInEditMode = true;

            headerLabel.setText("EDIT RECIPE");

            recipeNameTextField.setText(r.getName());
            preparationTimeSlider.setValue(r.getDuration());
            directionsTextArea.setText(r.getDescription());
        } else
            r = new Recipe();
        this.r = r;
    }

    @FXML
    public void initialize() {
        preparationTimeSlider.valueProperty().addListener((ChangeListener) -> preparationTimeLabel.textProperty().setValue(
        String.valueOf((int) preparationTimeSlider.getValue()) + " minutes"));
    }

    @FXML
    public void onSaveButtonClicked() {
        LOG.info("Save recipe button clicked");

        r.setName(recipeNameTextField.getText());
        r.setDuration(preparationTimeSlider.getValue());
        r.setDescription(directionsTextArea.getText());


        try {
            if (isInEditMode)
                recipeService.update(r);
            else
                recipeService.create(r);

            showAlert(Alert.AlertType.INFORMATION, "Saving successful.", "Recipe successfully saved.");
            LOG.debug("Recipe successfully saved.");
            ((Stage) saveButton.getScene().getWindow()).close();
        } catch (ServiceInvokationException e) {
            showAlert(Alert.AlertType.WARNING, "Error.", e.getContext().getErrors().toString());
            LOG.error(e.getMessage());
        }
    }

    @FXML
    public void onCancelButtonClicked() {
        LOG.debug("Recipe dialog cancelled.");
        ((Stage) saveButton.getScene().getWindow()).close();
    }
}
