package at.ac.tuwien.sepm.assignment.groupphase.application.ui;

import at.ac.tuwien.sepm.assignment.groupphase.application.dto.Recipe;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.RecipeService;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.ServiceInvokationException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
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

import static at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation.UserInterfaceUtility.showAlert;

@Controller
public class TabRecipesController {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private AnnotationConfigApplicationContext context;

    private RecipeService recipeService;

    @FXML
    Button addRecipeButton;

    @FXML
    TableView<Recipe> recipeTableView;

    @FXML
    TableColumn<Recipe, String> nameTableColumn;

    @FXML
    TableColumn<Recipe, Double> caloriesTableColumn;

    @FXML
    TableColumn<Recipe, Double> carbohydratesTableColumn;

    @FXML
    TableColumn<Recipe, Double> proteinsTableColumn;

    @FXML
    TableColumn<Recipe, Double> fatsTableColumn;

    @FXML
    TableColumn<Recipe, Double> preparationTimeTableColumn;

    @FXML
    ContextMenu recipeContextMenu;

    @FXML
    private ObservableList<Recipe> recipeObservableList = FXCollections.observableArrayList();

    public TabRecipesController(RecipeService recipeService){
        this.recipeService = recipeService;
    }


    @FXML
    public void initialize(){
        recipeTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        nameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        caloriesTableColumn.setCellValueFactory(new PropertyValueFactory<>("calories"));
        carbohydratesTableColumn.setCellValueFactory(new PropertyValueFactory<>("carbohydrates"));
        proteinsTableColumn.setCellValueFactory(new PropertyValueFactory<>("proteins"));
        fatsTableColumn.setCellValueFactory(new PropertyValueFactory<>("fats"));
        preparationTimeTableColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));

        recipeTableView.addEventHandler(MouseEvent.MOUSE_CLICKED, t -> {
            if (t.getButton() == MouseButton.SECONDARY) {
                recipeContextMenu.show(recipeTableView, t.getScreenX(), t.getScreenY());
            } else {
                recipeContextMenu.hide();
            }
        });

        updateRecipeTableView();

    }

    @FXML
    public void onEditRecipeClicked(ActionEvent actionEvent){
        LOG.info("Edit recipe button clicked");
        loadExternalController("/fxml/RecipeDetails.fxml", RecipeController.class, "Edit Recipe");
        updateRecipeTableView();
    }

    @FXML
    public void onAddRecipeButtonClicked(ActionEvent actionEvent) {
        LOG.info("Add recipe button clicked");
        loadExternalController("/fxml/RecipeDetails.fxml", RecipeController.class, "Add Recipe");
        updateRecipeTableView();
    }

    private void loadExternalController(String Path, Class c, String Title){
        context = new AnnotationConfigApplicationContext(c);
        try {
            FXMLLoader loader = new FXMLLoader();
            URL location = getClass().getResource(Path);
            loader.setLocation(location);
            loader.setControllerFactory(context::getBean);
            Stage stage = new Stage();

            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(addRecipeButton.getScene().getWindow());
            stage.setTitle(Title);
            stage.setScene(new Scene(loader.load()));
            stage.showAndWait();
        } catch (IOException e) {
            LOG.error(e.getMessage());
        }

    }

    private void updateRecipeTableView() {
        recipeObservableList.clear();
        try {
            recipeObservableList.addAll(recipeService.getRecipes());
        } catch (ServiceInvokationException e) {
            showAlert(Alert.AlertType.WARNING,"Error:", e.getMessage());
            LOG.error(e.getMessage());
        }

        recipeTableView.setItems(recipeObservableList);
    }
}
