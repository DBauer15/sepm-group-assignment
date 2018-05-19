package at.ac.tuwien.sepm.assignment.groupphase.application.service.implementation;

import at.ac.tuwien.sepm.assignment.groupphase.application.dto.DietPlan;
import at.ac.tuwien.sepm.assignment.groupphase.application.dto.Recipe;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.DietPlanPersistence;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.NoEntryFoundException;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.RecipePersistence;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.MealRecommendationsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Service
public class SimpleMealRecommendationsService implements MealRecommendationsService{

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final RecipePersistence recipePersistence;
    private final DietPlanPersistence dietPlanPersistence;

    public SimpleMealRecommendationsService(RecipePersistence recipePersistence, DietPlanPersistence dietPlanPersistence) {
        this.recipePersistence = recipePersistence;
        this.dietPlanPersistence = dietPlanPersistence;
    }

    @Override
    public List<Recipe> getRecommendedMeals() {

        try {
            DietPlan dietPlan = dietPlanPersistence.readActive();
            double[] limits = calculateLimits(dietPlan);






        } catch (PersistenceException e) {
            e.printStackTrace();
        } catch (NoEntryFoundException e) {
            e.printStackTrace();
        }


        return null;
    }
    
    private double[] calculateLimits(DietPlan dietPlan) {
        //@TODO: needs real caluclations
        double[] limits = new double[2];
        double lower_limit = dietPlan.getEnergy_kcal() - 100;
        double upper_limit = dietPlan.getEnergy_kcal() + 100;

        limits[0] = lower_limit;
        limits[1] = upper_limit;

        return limits;
    }
}
