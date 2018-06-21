package at.ac.tuwien.sepm.assignment.groupphase.application.ui;

import at.ac.tuwien.sepm.assignment.groupphase.application.dto.IngredientSearchParam;
import at.ac.tuwien.sepm.assignment.groupphase.application.dto.Recipe;
import at.ac.tuwien.sepm.assignment.groupphase.application.dto.RecipeImage;
import at.ac.tuwien.sepm.assignment.groupphase.application.dto.RecipeIngredient;
import at.ac.tuwien.sepm.assignment.groupphase.application.dto.RecipeTag;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.NotificationService;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.RecipeService;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.ServiceInvokationException;
import at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation.ImageUtil;
import at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation.JDBCConnectionManager;
import at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation.UserInterfaceUtility;
import at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation.ValidationUtilUi;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.nio.Buffer;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import javax.imageio.ImageIO;
import javax.sql.rowset.serial.SerialException;

import static at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation.UserInterfaceUtility.showAlert;

@Controller
public class RecipeController implements Initializable, ExternalController<Recipe> {
	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@FXML
	Label headerLabel;

	@FXML
	TextField ingredientAmountTextField;

	@FXML
	Label ingredientUnitLabel;

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

	@FXML
	TableView<RecipeIngredient> ingredientsTableView;

	@FXML
	TableColumn<RecipeIngredient, String> nameTableColumn;

	@FXML
	TableColumn<RecipeIngredient, String> unitTableColumn;

	@FXML
	TableColumn<RecipeIngredient, Double> amountTableColumn;

	@FXML
	TableColumn<RecipeIngredient, Object> buttonTableColumn;

	// Custom Ingredient
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
	@FXML
	private Pagination picturePagination;
	@FXML
	private Label noPictureChosenLabel;
	@FXML
	private Button removePicturesButton;

	private RecipeService recipeService;
	private NotificationService notificationService;
	private Recipe r;
	private boolean isInEditMode = false;
	private List<RecipeIngredient> ingredients;

	public RecipeController(RecipeService recipeService, NotificationService notificationService) {
		this.recipeService = recipeService;
		this.notificationService = notificationService;
	}

	public void onRemovePicturesButtonClicked() {
		this.r.getRecipeImages().clear();
		this.picturePagination.setPageCount(0);
		this.noPictureChosenLabel.setVisible(true);
		this.picturePagination.setVisible(false);
		this.removePicturesButton.setVisible(false);
	}

	public VBox displayPicture(int pageIndex) {
		ImageView imageView = new ImageView();

		if (r.getRecipeImages().size() > pageIndex) {
			BufferedImage bufferedImage = r.getRecipeImages().get(pageIndex).getImage();

			Image image = SwingFXUtils.toFXImage(bufferedImage, null);
			imageView.setImage(image);
			imageView.setFitHeight(200);
			imageView.setPreserveRatio(true);
			imageView.setSmooth(true);
			imageView.setCache(true);
		}

		VBox pictureBox = new VBox();
		pictureBox.getChildren().add(imageView);

		return pictureBox;
	}

	public void onAddPictureButtonClicked() {
		LOG.info("Add picture to recipe button clicked");

		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
				"JPEG files (*.jpeg), JPG files (*.jpg), PNG files (*.png)", "*.jpeg", "*.jpg", "*.png");
		fileChooser.getExtensionFilters().add(extFilter);

		List<File> pictures = fileChooser.showOpenMultipleDialog(saveButton.getScene().getWindow());

		if (pictures != null && pictures.size() != 0) {
			for (File p : pictures) {
				LOG.info("User has selected the following picture {}.", p.toString());
			}

			if (pictures.size() > 5) {
				showAlert(Alert.AlertType.ERROR, "Too many pictures", "You can only choose up to 5 images.");
			} else {
				this.r.getRecipeImages().clear();

				try {
					for (File p : pictures) {
						this.r.getRecipeImages().add(new RecipeImage(ImageIO.read(p), ImageUtil.getImageType(p)));
					}
				} catch (ServiceInvokationException ex) {
					UserInterfaceUtility.handleFaults(ex);
				} catch (IOException ex) {
					UserInterfaceUtility.handleFault(ex);
				}

				picturePagination.setPageCount(picturePagination.getPageCount() + 1);
				picturePagination.setPageCount(this.r.getRecipeImages().size());
				picturePagination.setVisible(true);
				noPictureChosenLabel.setVisible(false);
				removePicturesButton.setVisible(true);
			}
		} else {
			LOG.info("User has selected no picture.");
		}
	}

	public void initializeView(Recipe r) {
		this.ingredients = new ArrayList<>();
		ingredientsTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		nameTableColumn.setCellValueFactory(new PropertyValueFactory<>("ingredientName"));
		unitTableColumn.setCellValueFactory(new PropertyValueFactory<>("unitName"));
		amountTableColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
		buttonTableColumn.setCellFactory(param -> new TableCell<>() {
			Button removeButton = new Button("x");

            {
                removeButton.setStyle("-fx-background-radius: 50; " +
                    "-fx-font-size: 15; " +
                    "-fx-background-color: #E4E4E4;" +
                    "-fx-text-fill: #787878");
				removeButton.setOnAction(x -> {
					setGraphic(null);
					deleteIngredient(ingredientsTableView.getItems().get(getIndex()));
				});
			}

			@Override
			public void updateItem(Object item, boolean empty) {
				super.updateItem(item, empty);
				setGraphic(empty ? null : removeButton);
			}
		});

		picturePagination.setPageFactory(new Callback<Integer, Node>() {
			@Override
			public Node call(Integer pageIndex) {
				return displayPicture(pageIndex);
			}
		});

		if (r != null) {
			isInEditMode = true;

			headerLabel.setText("EDIT RECIPE");

			recipeNameTextField.setText(r.getName());
			preparationTimeSlider.setValue(r.getDuration() <= 120 ? r.getDuration() : (r.getDuration() / 60) + 118);
			/*
			 * "&#10" is a linefeed character, but for whatever reason this doesn't work
			 * when reading the string, therefor it has to be enforced manually. 2 line
			 * separators guarantee an empty line between paragraphs
			 */
			directionsTextArea
					.setText(r.getDescription().replace("&#10;", System.lineSeparator() + System.lineSeparator()));

			for (RecipeIngredient i : r.getRecipeIngredients()) {
				addIngredient(i);
			}

			if (r.getTags().contains(RecipeTag.B))
				breakfastCheckBox.setSelected(true);
			if (r.getTags().contains(RecipeTag.L))
				lunchCheckBox.setSelected(true);
			if (r.getTags().contains(RecipeTag.D))
				dinnerCheckBox.setSelected(true);
		} else {
			isInEditMode = false;
			r = new Recipe();
		}

		this.r = r;

		if (this.r.getRecipeImages().size() == 0) {
			this.noPictureChosenLabel.setVisible(true);
			this.picturePagination.setVisible(false);
			this.removePicturesButton.setVisible(false);
		} else {
			this.noPictureChosenLabel.setVisible(false);
			this.picturePagination.setVisible(true);
			this.picturePagination.setPageCount(this.r.getRecipeImages().size());
			this.removePicturesButton.setVisible(true);
		}
	}

	@FXML
	public void initialize(URL location, ResourceBundle resources) {
		preparationTimeSlider.valueProperty().addListener((ChangeListener) -> preparationTimeLabel.textProperty()
				.setValue(Math.floor(preparationTimeSlider.getValue()) < 120
						? String.valueOf((int) Math.floor(preparationTimeSlider.getValue())) + " minutes"
						: String.valueOf((int) Math.floor((preparationTimeSlider.getValue() - 118))) + " hours"));

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
		ingredientComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
			if (newValue != null) {
				ingredientUnitLabel.setText(newValue.getUnitName());
			}
		});

		customIngredientUnitChoiceBox.getSelectionModel().selectedItemProperty()
				.addListener((options, oldValue, newValue) -> {
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
			if (validateSearchIngredientInputs(recipeIngredient) == true) {
				recipeIngredient.setAmount(ValidationUtilUi.validateAndGet(ingredientAmountTextField.getText()));
				addIngredient(recipeIngredient);
			}
		} else {
			if (validateCustomIngredientInputs() == true) {
				RecipeIngredient recipeIngredient = createCustomIngredient();
				addIngredient(recipeIngredient);
			}
		}
	}

	@FXML
	public void onSaveButtonClicked() {
		LOG.info("Save recipe button clicked");

		r.setName(recipeNameTextField.getText());
		r.setDuration(preparationTimeSlider.getValue() < 120 ? (int) preparationTimeSlider.getValue()
				: (Math.floor(preparationTimeSlider.getValue()) - 118) * 60);
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
			if (isInEditMode) {
				recipeService.update(r);
			} else {
				recipeService.create(r);
			}

			showAlert(Alert.AlertType.INFORMATION, "Saving successful.", "Recipe successfully saved.");
			notificationService.notify(RecipeController.class);
			LOG.debug("Recipe successfully saved.");
			((Stage) saveButton.getScene().getWindow()).close();
		} catch (ServiceInvokationException e) {
			UserInterfaceUtility.handleFaults(e);
		} catch (Exception e) {
			UserInterfaceUtility.handleFault(e);
		}
	}

	@FXML
	public void onCancelButtonClicked() {
		LOG.debug("Recipe dialog cancelled.");
		((Stage) saveButton.getScene().getWindow()).close();
	}

	private boolean isCommonIngredientAlreadyPresent(RecipeIngredient newRi) {
		// checks if an common ingredient was already added - must be checked because
		// ingredient ids must be unique for every recipe (tuple key constraint)
		for (RecipeIngredient ri : ingredients) {
			if (ri.getId().intValue() == newRi.getId().intValue()) {
				return true;
			}
		}
		return false;
	}

	private void updateIngredientSearch(IngredientSearchParam searchParam) {
		try {
			List<RecipeIngredient> recipeIngredients = recipeService.searchIngredient(searchParam);
			ingredientComboBox.setItems(FXCollections.observableArrayList(recipeIngredients));
			ingredientComboBox.show();
		} catch (ServiceInvokationException e) {
			UserInterfaceUtility.handleFaults(e);
		} catch (Exception e) {
			UserInterfaceUtility.handleFault(e);
		}
	}

	private void addIngredient(RecipeIngredient recipeIngredient) {
		// add ingredient to tracking list
		ingredients.add(recipeIngredient);

		ingredientsTableView.getItems().add(recipeIngredient);

	}

	private void deleteIngredient(RecipeIngredient ingredient) {
		// remove from tracking list */
		ingredients.remove(ingredient);

		ingredientsTableView.getItems().remove(ingredient);
		// updateIngredientsTableView();
	}

	private boolean validateSearchIngredientInputs(RecipeIngredient recipeIngredient) {
		if (recipeIngredient == null) {
			showAlert(Alert.AlertType.ERROR, "Invalid Input",
					"Enter and choose a ingredient name, hit enter and then give me an amount to continue.");
			return false;
		}

		String amount = ingredientAmountTextField.getText();
		if (amount == null || amount.trim().length() == 0) {
			showAlert(Alert.AlertType.ERROR, "Information", "Ingredient amount must be set to a number.");
			return false;
		}
		Double amountDouble = ValidationUtilUi.validateAndGet(ingredientAmountTextField.getText());
		if (amountDouble == null || amountDouble <= 0) {
			showAlert(Alert.AlertType.ERROR, "Information",
					"Ingredient amount needs to be a true positive decimal number.");
			return false;
		}

		if (isCommonIngredientAlreadyPresent(recipeIngredient) == true) {
			showAlert(Alert.AlertType.ERROR, "Information",
					"This Ingredient has already been added to the recipe. If you want to change the amount, please correct your added ingredient.");
			return false;
		}

		return true;
	}

	private boolean validateCustomIngredientInputs() {
		Double amount = ValidationUtilUi.validateAndGet(customIngredientAmountTextField.getText());
		Double kcal = ValidationUtilUi.validateAndGet(customIngredientKcalTextField.getText());
		Double fat = ValidationUtilUi.validateAndGet(customIngredientFatTextField.getText());
		Double carbs = ValidationUtilUi.validateAndGet(customIngredientCarbsTextField.getText());
		Double protein = ValidationUtilUi.validateAndGet(customIngredientProteinTextField.getText());
		Double unitGrams = ValidationUtilUi.validateAndGet(customIngredientUnitGramsTextField.getText());
		String unitName = customIngredientUnitChoiceBox.getValue();
		String name = customIngredientNameTextField.getText();

		if (amount == null || amount <= 0) {
			showAlert(Alert.AlertType.ERROR, "Invalid Input", "Ingredient Amount must be a true positve number.");
			return false;
		}
		if (kcal == null || kcal < 0) {
			showAlert(Alert.AlertType.ERROR, "Invalid Input", "Ingredient Energy (kcal) must be a positve number.");
			return false;
		}
		if (validateNutrientRange(fat, "fat") == false) {
			return false;
		}
		if (validateNutrientRange(carbs, "carbohydrates") == false) {
			return false;
		}
		if (validateNutrientRange(protein, "protein") == false) {
			return false;
		}
		if (unitGrams == null || unitGrams <= 0) {
			showAlert(Alert.AlertType.ERROR, "Invalid Input", "Ingredient Unit Grams must be a positve number.");
			return false;
		}
		if (unitName == null || unitName.trim().length() <= 0) {
			showAlert(Alert.AlertType.ERROR, "Invalid Input", "Ingredient Unit Name must be selected.");
			return false;
		}
		if (name == null || name.trim().length() <= 0) {
			showAlert(Alert.AlertType.ERROR, "Invalid Input", "Ingredient Name must have at least 1 character.");
			return false;
		}

		final Double sumOfNutrients = fat + carbs + protein;
		if (sumOfNutrients > 100) {
			showAlert(Alert.AlertType.ERROR, "Invalid Input",
					"Sum of proteins, carbohydrates and fats per 100g must be lower than 100.");
			return false;
		}

		return true;
	}

	private boolean validateNutrientRange(Double nutrient, String name) {
		if (nutrient == null || nutrient < 0 || nutrient > 100) {
			showAlert(Alert.AlertType.ERROR, "Invalid Input",
					String.format("Ingredient %s/100g must be a a number between [0,100].", name));
			return false;
		}
		return true;
	}

	private RecipeIngredient createCustomIngredient() {
		Double amount = ValidationUtilUi.validateAndGet(customIngredientAmountTextField.getText());
		Double kcal = ValidationUtilUi.validateAndGet(customIngredientKcalTextField.getText());
		Double fat = ValidationUtilUi.validateAndGet(customIngredientFatTextField.getText());
		Double carbs = ValidationUtilUi.validateAndGet(customIngredientCarbsTextField.getText());
		Double protein = ValidationUtilUi.validateAndGet(customIngredientProteinTextField.getText());
		Double unitGrams = ValidationUtilUi.validateAndGet(customIngredientUnitGramsTextField.getText());
		String unitName = customIngredientUnitChoiceBox.getValue();
		String name = customIngredientNameTextField.getText();

		return new RecipeIngredient(amount, kcal, fat, protein, carbs, unitName, unitGrams, true, name);
	}

	private boolean isCustomIngredientMode() {
		return customIngredientAnchorPane.isVisible();
	}
}
