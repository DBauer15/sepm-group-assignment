package at.ac.tuwien.sepm.assignment.groupphase.application.ui;

import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import at.ac.tuwien.sepm.assignment.groupphase.application.dto.Recipe;
import at.ac.tuwien.sepm.assignment.groupphase.application.dto.RecipeTag;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.MealRecommendationsService;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.ServiceInvokationException;
import at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation.UserInterfaceUtility;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

@Controller
public class TabPlansController {
	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	private MealRecommendationsService mealRecommendationsService;

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

	public TabPlansController(MealRecommendationsService mealRecommendationsService) {
		this.mealRecommendationsService = mealRecommendationsService;
	}

	@FXML
	public void initialize() {
		updatePlan();
	}

	private void updatePlan() {
		try {
			Recipe breakfast = null;
			Recipe lunch = null;
			Recipe dinner = null;

			for (Recipe recipe : this.mealRecommendationsService.getRecommendedMeals()) {
				if (recipe.getTags().contains(RecipeTag.B)) {
					if (breakfast == null) {
						breakfast = recipe;
					}
				} else if (recipe.getTags().contains(RecipeTag.L)) {
					if (lunch == null) {
						lunch = recipe;
					}
				} else if (recipe.getTags().contains(RecipeTag.D)) {
					if (dinner == null) {
						dinner = recipe;
					}
				}
			}

			if (breakfast != null) {
				this.breakfastRecipeNameLabel.setText(breakfast.getName());
				this.breakfastPreparationTimeLabel.setText(breakfast.getDuration() + "'");
				this.breakfastCaloriesLabel.setText(breakfast.getCalories() + " kcal");
				this.breakfastCarbohydratesLabel.setText(breakfast.getCarbohydrates() + " Carbohydrates");
				this.breakfastProteinsLabel.setText(breakfast.getProteins() + " Proteins");
				this.breakfastFatsLabel.setText(breakfast.getFats() + " Fats");
			}

			if (dinner != null) {
				this.dinnerRecipeNameLabel.setText(dinner.getName());
				this.dinnerPreparationTimeLabel.setText(dinner.getDuration() + "'");
				this.dinnerCaloriesLabel.setText(dinner.getCalories() + " kcal");
				this.dinnerCarbohydratesLabel.setText(dinner.getCarbohydrates() + " Carbohydrates");
				this.dinnerProteinsLabel.setText(dinner.getProteins() + " Proteins");
				this.dinnerFatsLabel.setText(dinner.getFats() + " Fats");
			}

			if (lunch != null) {
				this.lunchRecipeNameLabel.setText(dinner.getName());
				this.lunchPreparationTimeLabel.setText(dinner.getDuration() + "'");
				this.lunchCaloriesLabel.setText(dinner.getCalories() + " kcal");
				this.lunchCarbohydratesLabel.setText(dinner.getCarbohydrates() + " Carbohydrates");
				this.lunchProteinsLabel.setText(dinner.getProteins() + " Proteins");
				this.lunchFatsLabel.setText(dinner.getFats() + " Fats");
			}

			// TODO handle no recommendation case
		} catch (ServiceInvokationException e) {
			UserInterfaceUtility.handleFaults(e.getContext());
		} catch (Exception e) {
			UserInterfaceUtility.handleFault(e);
		}
	}
}