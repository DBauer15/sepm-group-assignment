package at.ac.tuwien.sepm.assignment.groupphase.application.ui;

import at.ac.tuwien.sepm.assignment.groupphase.main.MainApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.URL;

@Controller
public class TabRecipesController {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private AnnotationConfigApplicationContext context;

    @FXML
    Button addRecipeButton;


    @FXML
    public void onAddRecipeButtonClicked(ActionEvent actionEvent) {
        LOG.info("Add recipe button clicked");
        context = new AnnotationConfigApplicationContext(NewRecipeController.class);
        try {
            FXMLLoader loader = new FXMLLoader();
            URL location = getClass().getResource("/fxml/NewRecipe.fxml");
            loader.setLocation(location);
            loader.setControllerFactory(context::getBean);
            Stage stage = new Stage();

            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(addRecipeButton.getScene().getWindow());
            stage.setTitle("Add Recipe");
            stage.setScene(new Scene(loader.load()));
            stage.showAndWait();
        } catch (IOException e) {
            LOG.error(e.getMessage());
        }
    }
}
