package at.ac.tuwien.sepm.assignment.groupphase.application.ui;

import at.ac.tuwien.sepm.assignment.groupphase.application.dto.IngredientSearchParam;
import at.ac.tuwien.sepm.assignment.groupphase.application.dto.Recipe;
import at.ac.tuwien.sepm.assignment.groupphase.application.dto.RecipeIngredient;
import at.ac.tuwien.sepm.assignment.groupphase.application.dto.RecipeTag;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.RecipeService;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.ServiceInvokationException;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.util.*;

import static at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation.UserInterfaceUtility.showAlert;

@Controller
public class RecipeController implements Initializable {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @FXML
    Label headerLabel;

    @FXML
    TextField ingredientAmountTextField;

    @FXML
    Label ingredientUnitLabel;

    @FXML
    GridPane ingredientsGridPane;

    @FXML
    TextField recipeNameTextField;

    @FXML
    TextArea directionsTextArea;

    @FXML
    Slider preparationTimeSlider;

    @FXML
    Button switchIngredientModeButton;

    @FXML
    Button saveButton;

    @FXML
    Label preparationTimeLabel;

    @FXML
    CheckBox breakfastCheckBox;

    @FXML
    CheckBox lunchCheckBox;

    @FXML
    CheckBox dinnerCheckBox;

    @FXML
    AnchorPane searchIngredientAnchorPane;

    @FXML
    AnchorPane customIngredientAnchorPane;

    @FXML
    ComboBox<RecipeIngredient> ingredientComboBox;

    //Custom Ingredient
    @FXML
    TextField customIngredientNameTextField;
    @FXML
    TextField customIngredientUnitGramsTextField;
    @FXML
    TextField customIngredientKcalTextField;
    @FXML
    TextField customIngredientProteinTextField;
    @FXML
    TextField customIngredientCarbsTextField;
    @FXML
    TextField customIngredientFatTextField;
    @FXML
    TextField customIngredientAmountTextField;
    @FXML
    Label customIngredientUnitLabel;
    @FXML
    ChoiceBox<String> customIngredientUnitChoiceBox;

    private RecipeService recipeService;
    private Recipe r;
    private boolean isInEditMode = false;
    private List<RecipeIngredient> ingredients;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    void initializeView(Recipe r) {
        this.ingredients = new ArrayList<>();

        if (r != null) {
            isInEditMode = true;

            headerLabel.setText("EDIT RECIPE");

            recipeNameTextField.setText(r.getName());
            preparationTimeSlider.setValue(r.getDuration());
            directionsTextArea.setText(r.getDescription());

            for (RecipeIngredient i : r.getRecipeIngredients())
                addIngredient(i);

            if (r.getTags().contains(RecipeTag.B))
                breakfastCheckBox.setSelected(true);
            if (r.getTags().contains(RecipeTag.L))
                lunchCheckBox.setSelected(true);
            if (r.getTags().contains(RecipeTag.D))
                dinnerCheckBox.setSelected(true);
        } else
            r = new Recipe();
        this.r = r;
    }

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        preparationTimeSlider.valueProperty().addListener((ChangeListener) -> preparationTimeLabel.textProperty().setValue(
        String.valueOf((int) preparationTimeSlider.getValue()) + " minutes"));

        ingredientComboBox.setConverter(new StringConverter<RecipeIngredient>() {
            @Override
            public String toString(RecipeIngredient object) {
                if (object == null) {
                    return null;
                }
                return object.toString();
            }

            @Override
            public RecipeIngredient fromString(String string) {
                return ingredientComboBox.getSelectionModel().getSelectedItem();
            }
        });
        ingredientComboBox.getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> {
            if (newValue != null) {
                ingredientUnitLabel.setText(newValue.getUnitName());
            }
        });

        customIngredientUnitChoiceBox.getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> {
            if (newValue != null) {
                customIngredientUnitLabel.setText(newValue);
            }
        });
    }

    @FXML
    public void onIngredientComboBoxKeyPressed(KeyEvent event) {
        IngredientSearchParam searchParam = new IngredientSearchParam(ingredientComboBox.getEditor().getText());
        updateIngredientSearch(searchParam);
    }

    @FXML
    public void onSwitchIngredientModeButtonClicked(ActionEvent event) {
        switchIngredientModeButton.setText(isCustomIngredientMode() ? "Customize" : "Search");
        customIngredientAnchorPane.setVisible(!customIngredientAnchorPane.isVisible());
        searchIngredientAnchorPane.setVisible(!searchIngredientAnchorPane.isVisible());
    }

    @FXML
    public void onAddButtonClicked(ActionEvent event) {
        if (!isCustomIngredientMode()) {
            RecipeIngredient recipeIngredient = ingredientComboBox.getSelectionModel().getSelectedItem();
            if (recipeIngredient != null && validateSearchIngredientInputs()) {
                recipeIngredient.setAmount(Double.parseDouble(ingredientAmountTextField.getText()));
                addIngredient(recipeIngredient);
            }
        } else {
            if (validateCustomIngredientInputs()) {
                RecipeIngredient recipeIngredient = createCustomIngredient();
                addIngredient(recipeIngredient);
            }
        }
    }

    @FXML
    public void onSaveButtonClicked() {
        LOG.info("Save recipe button clicked");

        r.setName(recipeNameTextField.getText());
        r.setDuration(preparationTimeSlider.getValue());
        r.setDescription(directionsTextArea.getText());
        r.setRecipeIngredients(ingredients);

        EnumSet<RecipeTag> tags = EnumSet.noneOf(RecipeTag.class);
        if (breakfastCheckBox.isSelected())
            tags.add(RecipeTag.B);
        if (lunchCheckBox.isSelected())
            tags.add(RecipeTag.L);
        if (dinnerCheckBox.isSelected())
            tags.add(RecipeTag.D);
        r.setTags(tags);

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

    private void updateIngredientSearch(IngredientSearchParam searchParam) {
        try {
            List<RecipeIngredient> recipeIngredients = recipeService.searchIngredient(searchParam);
            ingredientComboBox.setItems(FXCollections.observableArrayList(recipeIngredients));
            ingredientComboBox.show();
        } catch (ServiceInvokationException e) {
            showAlert(Alert.AlertType.WARNING, "Error.", e.getContext().getErrors().toString());
            LOG.error(e.getMessage());
        }
    }

    private void addIngredient(RecipeIngredient recipeIngredient) {
        final int row = ingredientsGridPane.getRowCount();

        //Create UI elements to be added to the grid pane
        Button removeButton = new Button("X");
        removeButton.setOnAction( event -> {
            deleteIngredient(recipeIngredient);
        });

        Label name = new Label(recipeIngredient.getIngredientName());
        name.getStyleClass().add("text-white");
        Label amount = new Label(String.valueOf(recipeIngredient.getAmount()));
        amount.getStyleClass().add("text-white");
        Label unit = new Label(recipeIngredient.getUnitName());
        unit.getStyleClass().add("text-white");

        // add a new row to the grid pane
        ingredientsGridPane.addRow(row, name, amount, unit, removeButton);

        // add ingredient to tracking list
        ingredients.add(recipeIngredient);

    }

    private void deleteIngredient(RecipeIngredient ingredient) {
        int row = ingredients.indexOf(ingredient);

        Set<Node> deleteNodes = new HashSet<>();
        for (Node child : ingredientsGridPane.getChildren()) {
            // get index from child
            Integer rowIndex = GridPane.getRowIndex(child);

            // handle null values for index=0
            int r = rowIndex == null ? 0 : rowIndex;

            if (r > row) {
                // decrement rows for rows after the deleted row
                GridPane.setRowIndex(child, r-1);
            } else if (r == row) {
                // collect matching rows for deletion
                deleteNodes.add(child);
            }
        }

        // remove nodes from row
        ingredientsGridPane.getChildren().removeAll(deleteNodes);

        // remove from tracking list
        ingredients.remove(ingredient);
    }

    private boolean validateSearchIngredientInputs() {
        //TODO Berni und co
        if (ingredientAmountTextField.getText() == null) {
            showAlert(Alert.AlertType.ERROR, "Information", "Ingredient amount needs to be set.");
            return false;
        }

        try {
            Double.parseDouble(ingredientAmountTextField.getText());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Information", "Ingredient amount needs to be a decimal number.");
            return false;
        }
        return true;
    }

    private boolean validateCustomIngredientInputs() {
        //TODO Berni und co
        return true;
    }

    private RecipeIngredient createCustomIngredient() {
        Double amount = Double.parseDouble(customIngredientAmountTextField.getText());
        Double kcal = Double.parseDouble(customIngredientKcalTextField.getText());
        Double fat = Double.parseDouble(customIngredientFatTextField.getText());
        Double carbs = Double.parseDouble(customIngredientCarbsTextField.getText());
        Double protein = Double.parseDouble(customIngredientProteinTextField.getText());
        Double unitGrams = Double.parseDouble(customIngredientUnitGramsTextField.getText());
        String unitName = customIngredientUnitChoiceBox.getValue();
        String name = customIngredientNameTextField.getText();

        return new RecipeIngredient(amount, kcal, fat, protein, carbs, unitName, unitGrams, true, name);
    }

    private boolean isCustomIngredientMode() {
        return customIngredientAnchorPane.isVisible();
    }
}
