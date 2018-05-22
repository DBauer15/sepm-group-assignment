package at.ac.tuwien.sepm.assignment.groupphase.application.ui;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.util.Map.Entry;

import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.NoEntryFoundException;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.Notifiable;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import at.ac.tuwien.sepm.assignment.groupphase.application.dto.Recipe;
import at.ac.tuwien.sepm.assignment.groupphase.application.dto.RecipeTag;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.MealRecommendationsService;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.ServiceInvokationException;
import at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation.SpringFXMLLoader;
import at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation.UserInterfaceUtility;
import at.ac.tuwien.sepm.assignment.groupphase.main.MainApplication;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

@Controller
public class TabPlansController implements Notifiable {
	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	private MealRecommendationsService mealRecommendationsService;
	private NotificationService notificationService;

	@FXML
	private Label breakfastRecipeNameLabel;
	@FXML
	private Label lunchRecipeNameLabel;
	@FXML
	private Label dinnerRecipeNameLabel;
	@FXML
	private Label breakfastCaloriesLabel;
	@FXML
	private Label lunchCaloriesLabel;
	@FXML
	private Label dinnerCaloriesLabel;
	@FXML
	private Label breakfastPreparationTimeLabel;
	@FXML
	private Label lunchPreparationTimeLabel;
	@FXML
	private Label dinnerPreparationTimeLabel;
	@FXML
	private Label breakfastCarbohydratesLabel;
	@FXML
	private Label lunchCarbohydratesLabel;
	@FXML
	private Label dinnerCarbohydratesLabel;
	@FXML
	private Label breakfastProteinsLabel;
	@FXML
	private Label lunchProteinsLabel;
	@FXML
	private Label dinnerProteinsLabel;
	@FXML
	private Label breakfastFatsLabel;
	@FXML
	private Label lunchFatsLabel;
	@FXML
	private Label dinnerFatsLabel;

	private Recipe breakfast;
	private Recipe lunch;
	private Recipe dinner;

	public TabPlansController(MealRecommendationsService mealRecommendationsService, NotificationService notificationService) {
		this.mealRecommendationsService = mealRecommendationsService;
		this.notificationService = notificationService;
	}

	@FXML
	public void initialize() {
		updatePlan();
		notificationService.subscribeTo(ChoosePlanController.class, this);
    }

	@Override
    public void onNotify() {
	    updatePlan();
    }

	private void updatePlan() {
		this.breakfastRecipeNameLabel.setText(null);
		this.breakfastPreparationTimeLabel.setText(null);
		this.breakfastCaloriesLabel.setText(null);
		this.breakfastCarbohydratesLabel.setText(null);
		this.breakfastProteinsLabel.setText(null);
		this.breakfastFatsLabel.setText(null);

		this.lunchRecipeNameLabel.setText(null);
		this.lunchPreparationTimeLabel.setText(null);
		this.lunchCaloriesLabel.setText(null);
		this.lunchCarbohydratesLabel.setText(null);
		this.lunchProteinsLabel.setText(null);
		this.lunchFatsLabel.setText(null);

		this.dinnerRecipeNameLabel.setText(null);
		this.dinnerPreparationTimeLabel.setText(null);
		this.dinnerCaloriesLabel.setText(null);
		this.dinnerCarbohydratesLabel.setText(null);
		this.dinnerProteinsLabel.setText(null);
		this.dinnerFatsLabel.setText(null);

		try {
			for (Entry<RecipeTag, Recipe> entry : this.mealRecommendationsService.getRecommendedMeals().entrySet()) {
				if (RecipeTag.B.equals(entry.getKey())) {
					if (breakfast == null) {
						breakfast = entry.getValue();
						updateRecipeSuggestion(breakfast, breakfastRecipeNameLabel, breakfastPreparationTimeLabel, breakfastCaloriesLabel, breakfastCarbohydratesLabel, breakfastProteinsLabel, breakfastFatsLabel);
					}
				} else if (RecipeTag.L.equals(entry.getKey())) {
					if (lunch == null) {
						lunch = entry.getValue();
						updateRecipeSuggestion(lunch, lunchRecipeNameLabel, lunchPreparationTimeLabel, lunchCaloriesLabel, lunchCarbohydratesLabel, lunchProteinsLabel, lunchFatsLabel);
					}
				} else if (RecipeTag.D.equals(entry.getKey())) {
					if (dinner == null) {
						dinner = entry.getValue();
						updateRecipeSuggestion(dinner, dinnerRecipeNameLabel, dinnerPreparationTimeLabel, dinnerCaloriesLabel, dinnerCarbohydratesLabel, dinnerProteinsLabel, dinnerFatsLabel);
					}
				}
			}
		} catch (ServiceInvokationException e) {
			UserInterfaceUtility.handleFaults(e.getContext());
		} catch (Exception e) {
			UserInterfaceUtility.handleFault(e);
		}
	}

	private void updateRecipeSuggestion(Recipe recipe, Label recipeName, Label preparationTime, Label calories, Label carbohydrates, Label proteins, Label fats) {
		recipeName.setText(recipe.getName());
		preparationTime.setText((int) Math.ceil(recipe.getDuration()) + "'");
		calories.setText((int) Math.ceil(recipe.getCalories()) + " kcal");
		carbohydrates.setText((int) Math.ceil(recipe.getCarbohydrates()) + " Carbohydrates");
		proteins.setText((int) Math.ceil(recipe.getProteins()) + " Proteins");
		fats.setText((int) Math.ceil(recipe.getFats()) + " Fats");
	}

	@FXML
	public void onBreakfastClick() {
		LOG.info("View breakfast recipe");
		UserInterfaceUtility.loadExternalController("/fxml/RecipeDetails.fxml", "Edit Recipe", breakfast, breakfastRecipeNameLabel.getScene().getWindow(), RecipeController.class);
	}

	@FXML
	public void onLunchClick() {
		LOG.info("View lunch recipe");
		UserInterfaceUtility.loadExternalController("/fxml/RecipeDetails.fxml", "Edit Recipe", lunch, lunchRecipeNameLabel.getScene().getWindow(), RecipeController.class);
	}

	@FXML
	public void onDinnerClick() {
		LOG.info("View dinner recipe");
		UserInterfaceUtility.loadExternalController("/fxml/RecipeDetails.fxml", "Edit Recipe", dinner, dinnerRecipeNameLabel.getScene().getWindow(), RecipeController.class);
	}
}