package at.ac.tuwien.sepm.assignment.groupphase.application.ui;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javafx.beans.value.ObservableValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import at.ac.tuwien.sepm.assignment.groupphase.application.dto.IngredientSearchWord;
import at.ac.tuwien.sepm.assignment.groupphase.application.dto.Recipe;
import at.ac.tuwien.sepm.assignment.groupphase.application.dto.RecipeSearchParam;
import at.ac.tuwien.sepm.assignment.groupphase.application.dto.RecipeTag;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.Notifiable;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.NotificationService;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.RecipeService;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.ServiceInvokationException;
import at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation.SpringFXMLLoader;
import at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation.UserInterfaceUtility;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Slider;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import javax.swing.event.ChangeListener;

@Controller
public class TabRecipesController implements Notifiable {
	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private RecipeService recipeService;
	private NotificationService notificationService;

    @Autowired
	private SpringFXMLLoader fxmlLoader;

	@FXML
	Button addRecipeButton;

	@FXML
	Button searchRecipesButton;

	@FXML
	TableView<Recipe> recipeTableView;

	@FXML
	TableColumn<Recipe, String> nameTableColumn;

	@FXML
	TableColumn<Recipe, Integer> caloriesTableColumn;

	@FXML
	TableColumn<Recipe, Integer> carbohydratesTableColumn;

	@FXML
	TableColumn<Recipe, Integer> proteinsTableColumn;

	@FXML
	TableColumn<Recipe, Integer> fatsTableColumn;

	@FXML
	TableColumn<Recipe, Integer> preparationTimeTableColumn;

	@FXML
	Slider lowerLimit;

	@FXML
	Slider upperLimit;

	@FXML
	Label lowerLimitLabel;

	@FXML
	Label upperLimitLabel;

	@FXML
	TextField recipeTitle;

	@FXML
	TextField addIngredient;

	@FXML
	ToggleButton tag_b;

	@FXML
	ToggleButton tag_d;

	@FXML
	ToggleButton tag_l;

	@FXML
	TableView<IngredientSearchWord> ingredientWordsView;

	@FXML
	TableColumn<IngredientSearchWord, String> ingredientTag;

	@FXML
	TableColumn<IngredientSearchWord, Object> ingredientTagRemove;

	private Set<String> ingredientTagSet = new HashSet<>();

	@FXML
	Pane paneSearch;

	@FXML
	AnchorPane anchorPane;

	@FXML
	private ObservableList<Recipe> recipeObservableList = FXCollections.observableArrayList();

	private RecipeSearchParam param = new RecipeSearchParam();

	public TabRecipesController(RecipeService recipeService, NotificationService notificationService) {
		this.recipeService = recipeService;
		this.notificationService = notificationService;
	}

	@FXML
	public void initialize() {
		recipeTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		nameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		caloriesTableColumn.setCellValueFactory(
				x -> new SimpleIntegerProperty((int) Math.ceil(x.getValue().getCalories())).asObject());
		carbohydratesTableColumn.setCellValueFactory(
				x -> new SimpleIntegerProperty((int) Math.ceil(x.getValue().getCarbohydrates())).asObject());
		proteinsTableColumn.setCellValueFactory(
				(x -> new SimpleIntegerProperty((int) Math.ceil(x.getValue().getProteins())).asObject()));
		fatsTableColumn.setCellValueFactory(
				x -> new SimpleIntegerProperty((int) Math.ceil(x.getValue().getFats())).asObject());
        preparationTimeTableColumn.setCellValueFactory(
            (x -> new SimpleIntegerProperty((int) Math.floor(x.getValue().getDuration())).asObject()));

		recipeTableView.setRowFactory(tableView -> {
			final TableRow<Recipe> row = new TableRow<>();

			final ContextMenu recipeContextMenu = new ContextMenu();
			final MenuItem editMenuItem = new MenuItem("Show and edit");
			editMenuItem.setOnAction(event -> onEditRecipeClicked(row.getItem()));
			recipeContextMenu.getItems().add(editMenuItem);

			final MenuItem deleteMenuItem = new MenuItem("Delete");
			deleteMenuItem.setOnAction(event -> onDeleteRecipeClicked(row.getItem()));
			recipeContextMenu.getItems().add(deleteMenuItem);

			row.contextMenuProperty()
					.bind(Bindings.when(row.emptyProperty()).then((ContextMenu) null).otherwise(recipeContextMenu));

            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2)
                    onEditRecipeClicked(row.getItem());
            });

            return row;
		});

        notificationService.subscribeTo(RecipeController.class, this);

		updateRecipeTableView();



		// ingredient search words
		ingredientWordsView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		ingredientTag.setCellValueFactory(new PropertyValueFactory<>("ingredientTag"));
		ingredientTagRemove.setCellFactory(param -> new TableCell<>() {
            Button removeButton = new Button("X");


            {
                removeButton.setStyle("-fx-background-radius: 50; " +
                    "-fx-font-size: 15; " +
                    "-fx-background-color: #E4E4E4;" +
                    "-fx-text-fill: #787878;");
                removeButton.setOnAction(x -> {
                    setGraphic(null);
                    deleteIngredient(ingredientWordsView.getItems().get(getIndex()));
                });
            }

            @Override
            public void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : removeButton);
            }

			private void deleteIngredient(IngredientSearchWord word) {
				LOG.info("Triggered Removal of ingredient tag '{}'", word.getIngredientTag());
				ingredientTagSet.remove(word.getIngredientTag());
		        ingredientWordsView.getItems().remove(word);
			}
        });

		lowerLimit.valueProperty()
				.addListener((ChangeListener) -> lowerLimitLabel.textProperty()
						.setValue(Math.floor(lowerLimit.getValue()) < 120
								? String.valueOf((int) Math.floor(lowerLimit.getValue())) + " minutes"
								: String.valueOf((int) Math.floor((lowerLimit.getValue() - 118))) + " hours"));

		upperLimit.valueProperty()
		.addListener((ChangeListener) -> upperLimitLabel.textProperty()
				.setValue(Math.floor(upperLimit.getValue()) < 120
						? String.valueOf((int) Math.floor(upperLimit.getValue())) + " minutes"
						: String.valueOf((int) Math.floor((upperLimit.getValue() - 118))) + " hours"));

		upperLimit.setValue(142.0);
		upperLimitLabel.setText("24 hours");

		this.paneSearch.setVisible(false);
		recipeTableView.setPlaceholder(new Label("No recipes were found matching your search criteria."));
		ingredientWordsView.setPlaceholder(new Label(""));



	}

	private void onDeleteRecipeClicked(Recipe recipe) {
		LOG.info("Delete recipe button clicked");

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirm recipe deletion");
		alert.setHeaderText("Confirm recipe deletion");
		alert.setContentText("Do you really want to delete recipe '" + recipe.getName() + "'?");

		Optional<ButtonType> result = alert.showAndWait();

		if (result.get() == ButtonType.OK) {
			try {
				recipeService.delete(recipe.getId());
			} catch (ServiceInvokationException e) {
				UserInterfaceUtility.handleFaults(e);
			} catch (Exception e) {
				UserInterfaceUtility.handleFault(e);
			}

			updateRecipeTableView();
		}
	}

    @Override
    public void onNotify() {
        updateRecipeTableView();
    }

	private void onEditRecipeClicked(Recipe recipe) {
		LOG.info("Edit recipe button clicked");
		UserInterfaceUtility.loadExternalController("/fxml/RecipeDetails.fxml", "Edit Recipe", recipe, addRecipeButton.getScene().getWindow(), RecipeController.class, fxmlLoader);
		updateRecipeTableView();
	}

	@FXML
	public void onAddRecipeButtonClicked(ActionEvent actionEvent) {
		LOG.info("Add recipe button clicked");
		UserInterfaceUtility.loadExternalController("/fxml/RecipeDetails.fxml", "Add Recipe", null, addRecipeButton.getScene().getWindow(), RecipeController.class, fxmlLoader);
		updateRecipeTableView();
	}

	@FXML
	public void onSearchRecipeButtonClicked(ActionEvent actionEvent) {
		LOG.info("On Search Recipe button clicked");

		// make search visible, but do not trigger search
		if (!this.paneSearch.isVisible()) {
			togglePaneSearch();
			return;
		}else{
            togglePaneSearch();
        }

		param = new RecipeSearchParam();
		param.setLowerDurationInkl(getSliderValue(lowerLimit));
		param.setUpperDurationInkl(getSliderValue(upperLimit));
		param.setRecipeName(this.recipeTitle.getText());
		EnumSet<RecipeTag> tags = EnumSet.noneOf(RecipeTag.class);
		if (tag_b.isSelected()) {
			tags.add(RecipeTag.B);
		}
		if (tag_l.isSelected()) {
			tags.add(RecipeTag.L);
		}
		if (tag_d.isSelected()) {
			tags.add(RecipeTag.D);
		}
		param.setTags(tags);
		for (String ingredient : this.ingredientTagSet)
			param.addIngredient(ingredient);
		LOG.info("Prepared new search param:\r\n{}", param.toString());

		updateRecipeTableView();


	}

	private void togglePaneSearch() {
		if (this.paneSearch.isVisible()) {
			AnchorPane.setBottomAnchor(recipeTableView, 0d);
		} else {
			AnchorPane.setBottomAnchor(recipeTableView, 169d);
		}
		this.paneSearch.setVisible(!this.paneSearch.isVisible());
	}

	private double getSliderValue(Slider slider) {
		return slider.getValue() < 120 ? (int) slider.getValue() : (Math.floor(slider.getValue()) - 118) * 60;
	}

	private List<Recipe> searchRecipes(RecipeSearchParam param) {
		List<Recipe> searchResult = new ArrayList<>();
		LOG.info("Now searching for recipes matching param:\r\n{}", param.toString());
		try {
			searchResult = recipeService.searchRecipes(param);
		} catch (ServiceInvokationException e) {
			UserInterfaceUtility.handleFaults(e);
		} catch (Exception e) {
			UserInterfaceUtility.handleFault(e);
		}
		LOG.info("Found {} recipes matching the criteria", searchResult.size());
		return searchResult;
	}


	public void onAddIngredient(KeyEvent e) {
		if (e.getCode() == KeyCode.ENTER) {
			String inputTag = addIngredient.getText().trim();
			LOG.info("Triggered add ingredient search word for '{}'.", inputTag);
			if (ingredientTagSet.contains(inputTag) == false) {
				ingredientTagSet.add(inputTag);
				ingredientWordsView.getItems().add(new IngredientSearchWord(inputTag));
			}
	        addIngredient.setText("");
		}
	}

	private void updateRecipeTableView() {
		recipeObservableList.clear();
		// do not use getRecipes here anymore ... use search instead
		List<Recipe> searchRecipes = searchRecipes(param);
		LOG.info("Refreshing the recipes table view with the found recipes.");
		recipeObservableList.addAll(searchRecipes);
		recipeTableView.setItems(recipeObservableList);
		recipeTableView.sort();
	}
}

