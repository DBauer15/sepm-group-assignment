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
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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
    private Button breakfastSwapButton;
    @FXML
    private Button lunchSwapButton;
    @FXML
    private Button dinnerSwapButton;
    @FXML
    private ImageView breakfastImageView;
    @FXML
    private ImageView dinnerImageView;
    @FXML
    private ImageView lunchImageView;

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
		notificationService.subscribeTo(RecipeController.class, this);
    }

	@Override
    public void onNotify() {
	    updatePlan();
        notificationService.notify(TabStatisticController.class);
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
						updateRecipeSuggestion(breakfast, breakfastRecipeNameLabel, breakfastPreparationTimeLabel, breakfastCaloriesLabel, breakfastCarbohydratesLabel, breakfastProteinsLabel, breakfastFatsLabel, breakfastImageView);
				} else if (RecipeTag.L.equals(entry.getKey())) {
						lunch = entry.getValue();
						updateRecipeSuggestion(lunch, lunchRecipeNameLabel, lunchPreparationTimeLabel, lunchCaloriesLabel, lunchCarbohydratesLabel, lunchProteinsLabel, lunchFatsLabel, lunchImageView);
				} else if (RecipeTag.D.equals(entry.getKey())) {
						dinner = entry.getValue();
						updateRecipeSuggestion(dinner, dinnerRecipeNameLabel, dinnerPreparationTimeLabel, dinnerCaloriesLabel, dinnerCarbohydratesLabel, dinnerProteinsLabel, dinnerFatsLabel, dinnerImageView);
				}
			}
		} catch (ServiceInvokationException e) {
			UserInterfaceUtility.handleFault(e);
		} catch (NoEntryFoundException e) {
		    LOG.warn("No active diet plan set. Skipping meal recommendations");
        } catch (Exception e) {
			UserInterfaceUtility.handleFault(e);
		}
	}

	private void updateRecipeSuggestion(Recipe recipe, Label recipeName, Label preparationTime, Label calories, Label carbohydrates, Label proteins, Label fats, ImageView imageView) {
		recipeName.setText(recipe.getName());
		preparationTime.setText(recipe.getDuration() < 120 ? (int) Math.floor(recipe.getDuration()) + "'" :
            (int) Math.floor((recipe.getDuration()/60)) + "''");
		calories.setText((int) Math.ceil(recipe.getCalories()) + " kcal");
		carbohydrates.setText((int) Math.ceil(recipe.getCarbohydrates()) + "g Carbohydrates");
		proteins.setText((int) Math.ceil(recipe.getProteins()) + "g Proteins");
		fats.setText((int) Math.ceil(recipe.getFats()) + "g Fats");

		if (recipe.getRecipeImages().size() > 0) {
			Image image = SwingFXUtils.toFXImage(recipe.getRecipeImages().get(0).getImage(), null);
            imageView.setPreserveRatio(true);
            imageView.setImage(image);
			imageView.setPreserveRatio(true);
			imageView.setSmooth(true);
			imageView.setCache(true);
		} else {
			imageView.setImage(null);
		}
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
    public void onSwapClicked(ActionEvent event) {
        Button source = (Button)event.getSource();

        if (source.equals(breakfastSwapButton)) {
            LOG.debug("Swap button on breakfast clicked");
            try {
                breakfast = mealRecommendationsService.getRecommendedMeal(RecipeTag.B, breakfast);
                updateBreakfast();
            } catch (ServiceInvokationException e) {
                UserInterfaceUtility.handleFaults(e);
            } catch (NoOptimalSolutionException e) {
                LOG.warn("No additional recipes found for breakfast: {}", e.getMessage());
                breakfastSwapButton.setDisable(true);
            }

        } else if (source.equals(lunchSwapButton)) {
            LOG.debug("Swap button on lunch clicked");
            try {
                lunch = mealRecommendationsService.getRecommendedMeal(RecipeTag.L, lunch);
                updateLunch();
            } catch (ServiceInvokationException e) {
                UserInterfaceUtility.handleFaults(e);
            } catch (NoOptimalSolutionException e) {
                LOG.warn("No additional recipes found for lunch: {}", e.getMessage());
                breakfastSwapButton.setDisable(true);
            }

        } else if (source.equals(dinnerSwapButton)) {
            LOG.debug("Swap button on dinner clicked");
            try {
                dinner = mealRecommendationsService.getRecommendedMeal(RecipeTag.D, dinner);
                updateDinner();
            } catch (ServiceInvokationException e) {
                UserInterfaceUtility.handleFaults(e);
            } catch (NoOptimalSolutionException e) {
                LOG.warn("No additional recipes found for dinner: {}", e.getMessage());
                breakfastSwapButton.setDisable(true);
            }
        }

        notificationService.notify(TabStatisticController.class);
    }

    private void updateBreakfast() {
        updateRecipeSuggestion(breakfast, breakfastRecipeNameLabel, breakfastPreparationTimeLabel, breakfastCaloriesLabel, breakfastCarbohydratesLabel, breakfastProteinsLabel, breakfastFatsLabel, breakfastImageView);
    }

    private void updateLunch() {
        updateRecipeSuggestion(lunch, lunchRecipeNameLabel, lunchPreparationTimeLabel, lunchCaloriesLabel, lunchCarbohydratesLabel, lunchProteinsLabel, lunchFatsLabel, lunchImageView);
    }

    private void updateDinner() {
        updateRecipeSuggestion(dinner, dinnerRecipeNameLabel, dinnerPreparationTimeLabel, dinnerCaloriesLabel, dinnerCarbohydratesLabel, dinnerProteinsLabel, dinnerFatsLabel, dinnerImageView);
    }
}