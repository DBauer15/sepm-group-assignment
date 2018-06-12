package at.ac.tuwien.sepm.assignment.groupphase.application.ui;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import at.ac.tuwien.sepm.assignment.groupphase.application.dto.Recipe;
import at.ac.tuwien.sepm.assignment.groupphase.application.dto.RecipeTag;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.NoEntryFoundException;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.MealRecommendationsService;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.NoOptimalSolutionException;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.Notifiable;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.NotificationService;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.ServiceInvokationException;
import at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation.SpringFXMLLoader;
import at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation.UserInterfaceUtility;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

@Controller
public class TabPlansController implements Notifiable {
	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	private MealRecommendationsService mealRecommendationsService;
	private NotificationService notificationService;
	
    @Autowired
	private SpringFXMLLoader fxmlLoader;

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
	@FXML
    private Button breakfastLeftButton;
    @FXML
    private Button breakfastRightButton;
    @FXML
    private Button lunchLeftButton;
    @FXML
    private Button lunchRightButton;
    @FXML
    private Button dinnerLeftButton;
    @FXML
    private Button dinnerRightButton;


	private Recipe breakfast;
	private List<Recipe> breakfasts;
	private Recipe lunch;
	private List<Recipe> lunches;
	private Recipe dinner;
	private List<Recipe> dinners;

	public TabPlansController(MealRecommendationsService mealRecommendationsService, NotificationService notificationService) {
		this.mealRecommendationsService = mealRecommendationsService;
		this.notificationService = notificationService;

		breakfasts = new ArrayList<>();
		lunches = new ArrayList<>();
		dinners = new ArrayList<>();
	}

	@FXML
	public void initialize() {
		updatePlan();
		notificationService.subscribeTo(ChoosePlanController.class, this);
		notificationService.subscribeTo(RecipeController.class, this);
    }

	@Override
    public void onNotify() {
	    breakfasts.clear();
	    lunches.clear();
	    dinners.clear();
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
						breakfast = entry.getValue();
						breakfasts.add(breakfast);
						updateRecipeSuggestion(breakfast, breakfastRecipeNameLabel, breakfastPreparationTimeLabel, breakfastCaloriesLabel, breakfastCarbohydratesLabel, breakfastProteinsLabel, breakfastFatsLabel);
				} else if (RecipeTag.L.equals(entry.getKey())) {
						lunch = entry.getValue();
						lunches.add(lunch);
						updateRecipeSuggestion(lunch, lunchRecipeNameLabel, lunchPreparationTimeLabel, lunchCaloriesLabel, lunchCarbohydratesLabel, lunchProteinsLabel, lunchFatsLabel);
				} else if (RecipeTag.D.equals(entry.getKey())) {
						dinner = entry.getValue();
						dinners.add(dinner);
						updateRecipeSuggestion(dinner, dinnerRecipeNameLabel, dinnerPreparationTimeLabel, dinnerCaloriesLabel, dinnerCarbohydratesLabel, dinnerProteinsLabel, dinnerFatsLabel);
				}
			}
		} catch (ServiceInvokationException e) {
			UserInterfaceUtility.handleFaults(e.getContext());
		} catch (NoEntryFoundException e) {
		    LOG.warn("No active diet plan set. Skipping meal recommendations");
        } catch (Exception e) {
			UserInterfaceUtility.handleFault(e);
		}
	}

	private void updateRecipeSuggestion(Recipe recipe, Label recipeName, Label preparationTime, Label calories, Label carbohydrates, Label proteins, Label fats) {
		recipeName.setText(recipe.getName());
		preparationTime.setText(recipe.getDuration() < 120 ? (int) Math.floor(recipe.getDuration()) + "'" :
            (int) Math.floor((recipe.getDuration()/60)) + "''");
		calories.setText((int) Math.ceil(recipe.getCalories()) + " kcal");
		carbohydrates.setText((int) Math.ceil(recipe.getCarbohydrates()) + "g Carbohydrates");
		proteins.setText((int) Math.ceil(recipe.getProteins()) + "g Proteins");
		fats.setText((int) Math.ceil(recipe.getFats()) + "g Fats");
	}

	@FXML
	public void onBreakfastClick() {
		LOG.info("View breakfast recipe");
		UserInterfaceUtility.loadExternalController("/fxml/RecipeDetails.fxml", "Edit Recipe", breakfast,
				breakfastRecipeNameLabel.getScene().getWindow(), RecipeController.class, fxmlLoader);
	}

	@FXML
	public void onLunchClick() {
		LOG.info("View lunch recipe");
		UserInterfaceUtility.loadExternalController("/fxml/RecipeDetails.fxml", "Edit Recipe", lunch,
				lunchRecipeNameLabel.getScene().getWindow(), RecipeController.class, fxmlLoader);
	}

	@FXML
	public void onDinnerClick() {
		LOG.info("View dinner recipe");
		UserInterfaceUtility.loadExternalController("/fxml/RecipeDetails.fxml", "Edit Recipe", dinner,
				dinnerRecipeNameLabel.getScene().getWindow(), RecipeController.class, fxmlLoader);
	}

	@FXML
    public void onLeftClicked(ActionEvent event) {
	    Button source = (Button)event.getSource();

	    if (source.equals(breakfastLeftButton)) {
	        LOG.debug("Left button on breakfast clicked");
            int index = breakfasts.indexOf(breakfast) - 1;
            if (index >= 0) {
                 if (index == 0) {
                     breakfastLeftButton.setDisable(true);
                 }
                 breakfast = breakfasts.get(index);
                 updateBreakfast();
                 breakfastRightButton.setDisable(false);
            }
        } else if (source.equals(lunchLeftButton)) {
            LOG.debug("Left button on lunch clicked");
            int index = lunches.indexOf(lunch) - 1;
            if (index >= 0) {
                if (index == 0) {
                    lunchLeftButton.setDisable(true);
                }
                lunch = lunches.get(index);
                updateLunch();
                lunchRightButton.setDisable(false);
            }
        } else if (source.equals(dinnerLeftButton)) {
            LOG.debug("Left button on dinner clicked");
            int index = dinners.indexOf(dinner) - 1;
            if (index >= 0) {
                if (index == 0) {
                    dinnerLeftButton.setDisable(true);
                }
                dinner = dinners.get(index);
                updateDinner();
                dinnerRightButton.setDisable(false);
            }
        }
    }

    @FXML
    public void onRightClicked(ActionEvent event) {
        Button source = (Button)event.getSource();

        if (source.equals(breakfastRightButton)) {
            LOG.debug("Right button on breakfast clicked");
            int index = breakfasts.indexOf(breakfast) + 1;
            if (index == breakfasts.size()) {
                try {
                    breakfast = mealRecommendationsService.getRecommendedMeal(RecipeTag.B, breakfasts);
                    breakfasts.add(breakfast);
                    updateBreakfast();
                    breakfastLeftButton.setDisable(false);
                } catch (NoOptimalSolutionException e) {
                    breakfastRightButton.setDisable(true);
                } catch (ServiceInvokationException e) {
                    UserInterfaceUtility.handleFaults(e.getContext());
                }
            } else {
                breakfast = breakfasts.get(index);
                updateBreakfast();
                breakfastLeftButton.setDisable(false);
            }

        } else if (source.equals(lunchRightButton)) {
            LOG.debug("Right button on lunch clicked");
            int index = lunches.indexOf(lunch) + 1;
            if (index == lunches.size()) {
                try {
                    lunch = mealRecommendationsService.getRecommendedMeal(RecipeTag.L, lunches);
                    lunches.add(lunch);
                    updateLunch();
                    lunchLeftButton.setDisable(false);
                } catch (NoOptimalSolutionException e) {
                    lunchRightButton.setDisable(true);
                } catch (ServiceInvokationException e) {
                    UserInterfaceUtility.handleFaults(e.getContext());
                }
            } else {
                lunch = lunches.get(index);
                updateLunch();
                lunchLeftButton.setDisable(false);
            }

        } else if (source.equals(dinnerRightButton)) {
            LOG.debug("Right button on dinner clicked");
            int index = dinners.indexOf(dinner) + 1;
            if (index == dinners.size()) {
                try {
                    dinner = mealRecommendationsService.getRecommendedMeal(RecipeTag.D, dinners);
                    dinners.add(dinner);
                    updateDinner();
                    dinnerLeftButton.setDisable(false);
                } catch (NoOptimalSolutionException e) {
                    dinnerRightButton.setDisable(true);
                } catch (ServiceInvokationException e) {
                    UserInterfaceUtility.handleFaults(e.getContext());
                }
            } else {
                dinner = dinners.get(index);
                updateDinner();
                dinnerLeftButton.setDisable(false);
            }
        }
    }

    private void updateBreakfast() {
        updateRecipeSuggestion(breakfast, breakfastRecipeNameLabel, breakfastPreparationTimeLabel, breakfastCaloriesLabel, breakfastCarbohydratesLabel, breakfastProteinsLabel, breakfastFatsLabel);
    }

    private void updateLunch() {
        updateRecipeSuggestion(lunch, lunchRecipeNameLabel, lunchPreparationTimeLabel, lunchCaloriesLabel, lunchCarbohydratesLabel, lunchProteinsLabel, lunchFatsLabel);
    }

    private void updateDinner() {
        updateRecipeSuggestion(dinner, dinnerRecipeNameLabel, dinnerPreparationTimeLabel, dinnerCaloriesLabel, dinnerCarbohydratesLabel, dinnerProteinsLabel, dinnerFatsLabel);
    }
}