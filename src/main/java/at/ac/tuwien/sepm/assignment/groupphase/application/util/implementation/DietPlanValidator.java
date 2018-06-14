package at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation;

import at.ac.tuwien.sepm.assignment.groupphase.application.dto.DietPlan;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.ServiceInvokationContext;
import at.ac.tuwien.sepm.assignment.groupphase.application.util.Validator;
import org.springframework.stereotype.Component;

@Component
public class DietPlanValidator implements Validator<DietPlan> {
    @Override
    public boolean validateForCreation(DietPlan dietPlan, ServiceInvokationContext context) {
        if (ValidationUtil.validateNull("Diet plan", dietPlan, context)) {
            return context.isValid();
        }
        ValidationUtil.validateStringLength("Diet plan name", dietPlan.getName(), 1, 255, context);

        ValidationUtil.validateDoubleLimitsInclusive("Energy Kcal", dietPlan.getEnergy_kcal(), 0d, 10000d, context);
        ValidationUtil.validateDoubleLimitsInclusive("Fats", dietPlan.getLipid(), 0d, 100d, context);
        ValidationUtil.validateDoubleLimitsInclusive("Proteins", dietPlan.getProtein(), 0d, 100d, context);
        ValidationUtil.validateDoubleLimitsInclusive("Carbohydrates", dietPlan.getCarbohydrate(), 0d, 100d, context);

        if (dietPlan.getLipid() + dietPlan.getProtein() +  dietPlan.getCarbohydrate() != 100)
            context.addError("The sum of 'Carbohydrates', 'Proteins' and 'Fats' has to be equal to 100%");

        return context.isValid();
    }

    @Override
    public boolean validateForReading(DietPlan dietPlan, ServiceInvokationContext context) {
        if (ValidationUtil.validateNull("Diet plan", dietPlan, context)) {
            return context.isValid();
        }
        validateForUpdate(dietPlan, context);
        return context.isValid();
    }

    @Override
    public boolean validateForUpdate(DietPlan dietPlan, ServiceInvokationContext context) {
        if (ValidationUtil.validateNull("Diet plan", dietPlan, context)) {
            return context.isValid();
        }
        ValidationUtil.validateId(dietPlan.getId(), context);
        validateForCreation(dietPlan, context);

        return context.isValid();
    }
}
