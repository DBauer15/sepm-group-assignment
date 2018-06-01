package at.ac.tuwien.sepm.assignment.groupphase.application.ui;

import at.ac.tuwien.sepm.assignment.groupphase.application.dto.DietPlan;
import at.ac.tuwien.sepm.assignment.groupphase.application.dto.Recipe;
import at.ac.tuwien.sepm.assignment.groupphase.application.dto.RecipeTag;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.NoEntryFoundException;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.DietPlanService;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.NotificationService;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.ServiceInvokationContext;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.ServiceInvokationException;
import at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation.SpringFXMLLoader;
import at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation.UserInterfaceUtility;
import at.ac.tuwien.sepm.assignment.groupphase.main.MainApplication;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import org.apache.commons.validator.routines.DoubleValidator;
import org.apache.commons.validator.routines.IntegerValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import static at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation.UserInterfaceUtility.showAlert;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;

@Controller
public class DietPlanController implements ExternalController<DietPlan> {
	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	private DietPlan dp;

	@FXML
	public Label exitLabel;
	
	@FXML
	public Button createButton;

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

	public DietPlanController(DietPlanService dietPlanService, NotificationService notificationService) {
		this.dietPlanService = dietPlanService;
		this.notificationService = notificationService;
	}

	@FXML
	public void initialize() {
		exitLabel.setManaged(true);
	}

	public void onCreateClicked() {
		LOG.info("Save diet plan button clicked");
		IntegerValidator integerValidator = new IntegerValidator();
		DoubleValidator doubleValidator = new DoubleValidator();
		ServiceInvokationContext context = new ServiceInvokationContext();

		try {
			Integer kcal = integerValidator.validate(this.kcalTextField.getText());
			Double carbohydrate = doubleValidator.validate(this.carbohydratesTextField.getText().replace('.', ','));
			Double protein = doubleValidator.validate(this.proteinsTextField.getText().replace('.', ','));
			Double fats = doubleValidator.validate(this.fatsTextField.getText().replace('.', ','));

			if (kcal == null) {
				context.addError("Enter a value that is an integer in the field 'Energy Kcal'");
			}

			if (carbohydrate == null) {
				context.addError("Enter a value that is an integer in the field 'Carbohydrates'");
			}

			if (protein == null) {
				context.addError("Enter a value that is an integer in the field 'Proteins'");
			}

			if (fats == null) {
				context.addError("Enter a value that is an integer in the field 'Fats'");
			}

			if (!context.isValid()) {
				throw new ServiceInvokationException(context);
			}

			if (dp == null) {
				dp = new DietPlan(dietPlanNameTextField.getText(), kcal.doubleValue(), fats,
						protein, carbohydrate);
				
				dietPlanService.create(dp);
			} else {
				dp.setName(dietPlanNameTextField.getText());
				dp.setEnergy_kcal(kcal.doubleValue());
				dp.setLipid(fats);
				dp.setProtein(protein);
				dp.setCarbohydrate(carbohydrate);
				
				dietPlanService.update(dp);
			}

            dietPlanService.switchTo(dp);

			notificationService.notify(ChoosePlanController.class);
			LOG.debug("Diet plan successfully saved.");

			this.onExitClicked();
		} catch (ServiceInvokationException e) {
			UserInterfaceUtility.handleFaults(e.getContext());
		} catch (Exception e) {
			UserInterfaceUtility.handleFault(e);
		}
	}

	public void onCancelClicked() {
		LOG.info("Cancel diet plan creation/edit button clicked");

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
		if (object != null) {
			dp = object;
			
			this.dietPlanNameTextField.setText(dp.getName());
			this.kcalTextField.setText(formatDouble(dp.getEnergy_kcal()));
			this.carbohydratesTextField.setText(formatDouble(dp.getCarbohydrate()));
			this.proteinsTextField.setText(formatDouble(dp.getProtein()));
			this.fatsTextField.setText(formatDouble(dp.getLipid()));
			
			this.createButton.setText("Save");
		} else {
			this.createButton.setText("Create");
		}
	}
	
	public static String formatDouble(double d)
	{
	    if (d == (long) d) {
	        return String.format("%d", (long) d);
	    } else {
	        return String.format("%s", d);
	    }
	}
}