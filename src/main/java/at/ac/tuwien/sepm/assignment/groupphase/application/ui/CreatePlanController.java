package at.ac.tuwien.sepm.assignment.groupphase.application.ui;

import at.ac.tuwien.sepm.assignment.groupphase.application.dto.DietPlan;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.NoEntryFoundException;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.DietPlanService;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.NotificationService;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.ServiceInvokationException;
import at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation.SpringFXMLLoader;
import at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation.UserInterfaceUtility;
import at.ac.tuwien.sepm.assignment.groupphase.main.MainApplication;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Controller
public class CreatePlanController implements ExternalController<DietPlan> {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @FXML
    public Label exitLabel;

    @FXML
    public TextField dietPlanNameTextField;
    
    @FXML
    public TextField kcalTextField;
    
    @FXML
    public TextField carbohydratesTextField;
    
    @FXML
    public TextField proteinsTextField;

    @FXML
    public TextField fatsTextField;
    
    private DietPlanService dietPlanService;
    private NotificationService notificationService;

    public CreatePlanController(DietPlanService dietPlanService, NotificationService notificationService) {
        this.dietPlanService = dietPlanService;
        this.notificationService = notificationService;
    }

    @FXML
    public void initialize() {
    	exitLabel.setManaged(true);
    }

    public void onCreateClicked() {
    
    }

    public void onCancelClicked() {
        try {
            final var fxmlLoader = MainApplication.context.getBean(SpringFXMLLoader.class);
            String path = "/fxml/ChoosePlan.fxml";
            fxmlLoader.setLocation(getClass().getResource(path));
            Stage stage = (Stage) exitLabel.getScene().getWindow();
            stage.setTitle("Choose plan");
            stage.setScene(new Scene((Parent) fxmlLoader.load(getClass().getResourceAsStream(path))));
        } catch (IOException e) {
            UserInterfaceUtility.handleFault(e);
        }
    }

    public void onExitClicked() {
        ((Stage) exitLabel.getScene().getWindow()).close();
    }

	@Override
	public void initializeView(DietPlan object) {		
	}
}
