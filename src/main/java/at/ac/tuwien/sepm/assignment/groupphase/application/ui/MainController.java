package at.ac.tuwien.sepm.assignment.groupphase.application.ui;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import at.ac.tuwien.sepm.assignment.groupphase.application.dto.DietPlan;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.NoEntryFoundException;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.DietPlanService;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.ServiceInvokationException;
import at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation.SpringFXMLLoader;
import at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation.UserInterfaceUtility;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

@Controller
public class MainController {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
	private SpringFXMLLoader fxmlLoader;

    @FXML
    private ImageView dietPlanImageView;
    @FXML
    private Label titleLabel;
    @FXML
    private Label carbohydratesLabel;
    @FXML
    private Label proteinsLabel;
    @FXML
    private Label lipidsLabel;
    @FXML
    private Label kcalLabel;

    @FXML
    private AnchorPane mainAnchorPane;
    @FXML
    private TabPane mainTabPane;
    @FXML
    private TabPlansController tabPlansController;
    @FXML
    private TabRecipesController tabRecipesController;
    @FXML
    private TabStatisticController tabStatisticController;

    @FXML
    private Button changePlanButton;

    private DietPlanService dietPlanService;

    public MainController(DietPlanService dietPlanService) {
        this.dietPlanService = dietPlanService;
    }

    public void initializeView() {

        mainTabPane.tabMinWidthProperty().bind(mainAnchorPane.widthProperty().divide(mainTabPane.getTabs().size()).subtract(20));

        try {
            DietPlan active = dietPlanService.readActive();

            DecimalFormat nf = (DecimalFormat) NumberFormat.getNumberInstance(Locale.US);
            nf.applyPattern("##.#");

            // Set icons for default plans or custom icon for custom plan

            if (active.getId() == 1) {
                dietPlanImageView.setImage(new Image("/img/planBuildMuscle.png"));
            } else if (active.getId() == 2) {
                dietPlanImageView.setImage(new Image("/img/planLoseWeight.png"));
            } else if (active.getId() == 3) {
                dietPlanImageView.setImage(new Image("/img/planCarefree.png"));
            } else {
                dietPlanImageView.setImage(new Image("/img/planCustom.png"));
            }

            titleLabel.setText(active.getName());
            carbohydratesLabel.setText(nf.format(active.getCarbohydrate()) + " % Carbohydrates");
            proteinsLabel.setText(nf.format(active.getProtein()) + " % Proteins");
            lipidsLabel.setText(nf.format(active.getLipid()) + " % Fats");
            kcalLabel.setText(nf.format(active.getEnergy_kcal()) + " kcal");

        } catch (NoEntryFoundException e) {
            LOG.debug("{}, choose diet plan.", e.getMessage());
            loadExternalController();
            initializeView();
        } catch (ServiceInvokationException e) {
            UserInterfaceUtility.handleFaults(e);
        }
    }

    private void loadExternalController() {
        try {
            String path = "/fxml/ChoosePlan.fxml";
            fxmlLoader.setLocation(getClass().getResource(path));

            Stage stage = new Stage();
            stage.getIcons().add(new Image("/img/foodOrca.png"));
            stage.initStyle(StageStyle.UNDECORATED);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(changePlanButton.getScene().getWindow());
            stage.setOnCloseRequest(event -> LOG.debug("ChoosePlanController closed, checking for active diet plan."));
            stage.setTitle("Choose Plan");
            stage.setScene(new Scene((Parent) fxmlLoader.load(getClass().getResourceAsStream(path))));
            stage.showAndWait();
            stage.setResizable(false);
        } catch (IOException e) {
            UserInterfaceUtility.handleFault(e);
        }
    }

    public void onChangeDietPlanClicked() {
        LOG.debug("Change diet plan button clicked");
        loadExternalController();
        initializeView();
    }
}
