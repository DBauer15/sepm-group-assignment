package at.ac.tuwien.sepm.assignment.groupphase.application.ui;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import at.ac.tuwien.sepm.assignment.groupphase.application.dto.DietPlan;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.NoEntryFoundException;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.DietPlanService;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.NotificationService;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.ServiceInvokationException;
import at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation.SpringFXMLLoader;
import at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation.UserInterfaceUtility;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

@Controller
public class ChoosePlanController {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
	private SpringFXMLLoader fxmlLoader;
    
    @FXML
    public Label exitLabel;
    @FXML
    private AnchorPane dietPlanPane1;
    @FXML
    private AnchorPane dietPlanPane2;
    @FXML
    private AnchorPane dietPlanPane3;

    private DietPlanService dietPlanService;
    private NotificationService notificationService;

    private List<DietPlan> dietPlans;
    private DietPlan customDietPlan;

    public ChoosePlanController(DietPlanService dietPlanService, NotificationService notificationService) {
        this.dietPlanService = dietPlanService;
        this.notificationService = notificationService;
    }

    @FXML
    public void initialize() {
        try {
            dietPlanService.readActive();
            exitLabel.setManaged(true);
        } catch (ServiceInvokationException e) {
            UserInterfaceUtility.handleFaults(e.getContext());
        } catch (NoEntryFoundException e) {
            exitLabel.setManaged(false);
        }

        try {
            dietPlans = dietPlanService.readAll();
            List<AnchorPane> dietPlanPanes = Arrays.asList(dietPlanPane1, dietPlanPane2, dietPlanPane3);

            DecimalFormat nf = (DecimalFormat) NumberFormat.getNumberInstance(Locale.US);
            nf.applyPattern("##.#");

            for (int i = 0; i < dietPlans.size(); i++) {
                DietPlan dp = dietPlans.get(i);
                
                if (dp.getId() > 3) {
                	customDietPlan = dp;
                	continue;
                }
                
                AnchorPane ap = dietPlanPanes.get(i);

                // ImageView image = ((ImageView) ap.getChildren().get(0));
                ((Label) ap.getChildren().get(1)).setText(dp.getName());
                ((Label) ap.getChildren().get(2)).setText(nf.format(dp.getCarbohydrate()) + "% Carbohydrates");
                ((Label) ap.getChildren().get(3)).setText(nf.format(dp.getProtein()) + "% Proteins");
                ((Label) ap.getChildren().get(4)).setText(nf.format(dp.getLipid()) + "% Fats");
                ((Label) ap.getChildren().get(5)).setText(nf.format(dp.getEnergy_kcal()) + " kcal");
            }
        } catch (ServiceInvokationException e) {
            UserInterfaceUtility.handleFaults(e.getContext());
        } catch (Exception e) {
            UserInterfaceUtility.handleFault(e);
        }
    }

    public void onDietPlanClicked(MouseEvent mouseEvent) {
        String paneId = ((AnchorPane) mouseEvent.getSource()).getId();
        DietPlan selected;

        switch (paneId) {
            case "dietPlanPane1":
                selected = dietPlans.get(0);
                break;
            case "dietPlanPane2":
                selected = dietPlans.get(1);
                break;
            case "dietPlanPane3":
                selected = dietPlans.get(2);
                break;
            default:
                LOG.debug("{} doesn't exist", paneId);
                return;
        }

        LOG.debug("Clicked on {}", paneId);

        try {
            dietPlanService.switchTo(selected);
            notificationService.notify(ChoosePlanController.class);
            ((Stage) dietPlanPane1.getScene().getWindow()).close();
        } catch (ServiceInvokationException e) {
            UserInterfaceUtility.handleFaults(e.getContext());
        } catch (Exception e) {
            UserInterfaceUtility.handleFault(e);
        }
    }

    public void onExitClicked() {
        ((Stage) dietPlanPane1.getScene().getWindow()).close();
    }

    public void onCreateClicked() {
        try {
            String path = "/fxml/CreatePlan.fxml";
            fxmlLoader.setLocation(getClass().getResource(path));
            Stage stage = (Stage) exitLabel.getScene().getWindow();
            stage.setTitle("Custom diet plan");
            
			var load = fxmlLoader.loadAndWrap(DietPlanController.class.getResourceAsStream(path), DietPlanController.class);
			load.getController().initializeView(customDietPlan);
			stage.setScene(new Scene((Parent) load.getLoadedObject()));
        } catch (IOException e) {
            UserInterfaceUtility.handleFault(e);
        }
    }
}