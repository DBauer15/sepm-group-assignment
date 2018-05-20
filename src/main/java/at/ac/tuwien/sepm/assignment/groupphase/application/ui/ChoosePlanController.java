package at.ac.tuwien.sepm.assignment.groupphase.application.ui;

import at.ac.tuwien.sepm.assignment.groupphase.application.dto.DietPlan;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.DietPlanService;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.ServiceInvokationException;
import at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation.UserInterfaceUtility;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import java.lang.invoke.MethodHandles;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Controller
public class ChoosePlanController {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @FXML
    private AnchorPane dietPlanPane1;
    @FXML
    private AnchorPane dietPlanPane2;
    @FXML
    private AnchorPane dietPlanPane3;

    private DietPlanService dietPlanService;

    private List<DietPlan> dietPlans;

    public ChoosePlanController(DietPlanService dietPlanService) {
        this.dietPlanService = dietPlanService;
    }

    @FXML
    public void initialize() {
        try {
            dietPlans = dietPlanService.readAll();
            List<AnchorPane> dietPlanPanes = Arrays.asList(dietPlanPane1, dietPlanPane2, dietPlanPane3);

            DecimalFormat nf = (DecimalFormat) NumberFormat.getNumberInstance(Locale.US);
            nf.applyPattern("##.#");

            for (int i = 0; i < dietPlans.size(); i++) {
                DietPlan dp = dietPlans.get(i);
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
                LOG.debug(paneId + " doesn't exist");
                return;
        }

        LOG.debug("Clicked on " + paneId);

        try {
            dietPlanService.switchTo(selected);
            ((Stage) dietPlanPane1.getScene().getWindow()).close();
        } catch (ServiceInvokationException e) {
            UserInterfaceUtility.handleFaults(e.getContext());
        } catch (Exception e) {
            UserInterfaceUtility.handleFault(e);
        }
    }
}
