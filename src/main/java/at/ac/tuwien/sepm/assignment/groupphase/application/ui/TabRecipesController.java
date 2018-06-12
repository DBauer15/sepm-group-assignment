package at.ac.tuwien.sepm.assignment.groupphase.application.ui;

import at.ac.tuwien.sepm.assignment.groupphase.application.dto.Recipe;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.Notifiable;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.NotificationService;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.RecipeService;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.ServiceInvokationException;
import at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation.SpringFXMLLoader;
import at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation.UserInterfaceUtility;
import at.ac.tuwien.sepm.assignment.groupphase.main.MainApplication;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.util.Optional;

import static at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation.UserInterfaceUtility.showAlert;

@Controller
public class TabRecipesController implements Notifiable {
	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private RecipeService recipeService;
	private NotificationService notificationService;

	@FXML
	Button addRecipeButton;

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
	private ObservableList<Recipe> recipeObservableList = FXCollections.observableArrayList();

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
				UserInterfaceUtility.handleFaults(e.getContext());
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
		UserInterfaceUtility.loadExternalController("/fxml/RecipeDetails.fxml", "Edit Recipe", recipe, addRecipeButton.getScene().getWindow(), RecipeController.class);
		updateRecipeTableView();
	}

	@FXML
	public void onAddRecipeButtonClicked(ActionEvent actionEvent) {
		LOG.info("Add recipe button clicked");
		UserInterfaceUtility.loadExternalController("/fxml/RecipeDetails.fxml", "Add Recipe", null, addRecipeButton.getScene().getWindow(), RecipeController.class);
		updateRecipeTableView();
	}

	private void updateRecipeTableView() {
		recipeObservableList.clear();
		try {
			recipeObservableList.addAll(recipeService.getRecipes());
		} catch (ServiceInvokationException e) {
			UserInterfaceUtility.handleFaults(e.getContext());
		} catch (Exception e) {
			UserInterfaceUtility.handleFault(e);
		}

		recipeTableView.setItems(recipeObservableList);

		recipeTableView.sort();
	}
}
