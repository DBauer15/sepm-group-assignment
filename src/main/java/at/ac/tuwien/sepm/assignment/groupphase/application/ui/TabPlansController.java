package at.ac.tuwien.sepm.assignment.groupphase.application.ui;

import java.lang.invoke.MethodHandles;
import java.util.Map.Entry;

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
			Recipe breakfast = null;
			Recipe lunch = null;
			Recipe dinner = null;

			for (Entry<RecipeTag, Recipe> entry : this.mealRecommendationsService.getRecommendedMeals().entrySet()) {
				if (RecipeTag.B.equals(entry.getKey())) {
					if (breakfast == null) {
						breakfast = entry.getValue();
					}
				} else if (RecipeTag.L.equals(entry.getKey())) {
					if (lunch == null) {
						lunch = entry.getValue();
					}
				} else if (RecipeTag.D.equals(entry.getKey())) {
					if (dinner == null) {
						dinner = entry.getValue();
					}
				}
			}

			if (breakfast != null) {
				this.breakfastRecipeNameLabel.setText(breakfast.getName());
				this.breakfastPreparationTimeLabel.setText((int) Math.ceil(breakfast.getDuration()) + "'");
				this.breakfastCaloriesLabel.setText((int) Math.ceil(breakfast.getCalories()) + " kcal");
				this.breakfastCarbohydratesLabel.setText((int) Math.ceil(breakfast.getCarbohydrates()) + " Carbohydrates");
				this.breakfastProteinsLabel.setText((int) Math.ceil(breakfast.getProteins()) + " Proteins");
				this.breakfastFatsLabel.setText((int) Math.ceil(breakfast.getFats()) + " Fats");
			}

			if (dinner != null) {
				this.dinnerRecipeNameLabel.setText(dinner.getName());
				this.dinnerPreparationTimeLabel.setText((int) Math.ceil(dinner.getDuration()) + "'");
				this.dinnerCaloriesLabel.setText((int) Math.ceil(dinner.getCalories()) + " kcal");
				this.dinnerCarbohydratesLabel.setText((int) Math.ceil(dinner.getCarbohydrates()) + " Carbohydrates");
				this.dinnerProteinsLabel.setText((int) Math.ceil(dinner.getProteins()) + " Proteins");
				this.dinnerFatsLabel.setText((int) Math.ceil(dinner.getFats()) + " Fats");
			}

			if (lunch != null) {
				this.lunchRecipeNameLabel.setText(dinner.getName());
				this.lunchPreparationTimeLabel.setText((int) Math.ceil(dinner.getDuration()) + "'");
				this.lunchCaloriesLabel.setText((int) Math.ceil(dinner.getCalories()) + " kcal");
				this.lunchCarbohydratesLabel.setText((int) Math.ceil(dinner.getCarbohydrates()) + " Carbohydrates");
				this.lunchProteinsLabel.setText((int) Math.ceil(dinner.getProteins()) + " Proteins");
				this.lunchFatsLabel.setText((int) Math.ceil(dinner.getFats()) + " Fats");
			}

			// TODO handle no recommendation case
		} catch (ServiceInvokationException e) {
			UserInterfaceUtility.handleFaults(e.getContext());
		} catch (Exception e) {
			UserInterfaceUtility.handleFault(e);
		}
	}
}