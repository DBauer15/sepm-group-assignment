package at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation;

import at.ac.tuwien.sepm.assignment.groupphase.application.dto.DietPlan;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.ServiceInvokationContext;
import at.ac.tuwien.sepm.assignment.groupphase.application.util.Validator;

public class DietPlanValidator implements Validator<DietPlan> {
    @Override
    public boolean validateForCreation(DietPlan dietPlan, ServiceInvokationContext context) {
        if (ValidationUtil.validateNull("Diet plan", dietPlan, context)) {
            return context.isValid();
        }
        ValidationUtil.validateStringLength("Diet plan name", dietPlan.getName(), 5, 255, context);

        // TODO was sind sinnvolle schranken?
        ValidationUtil.validateDoubleLimits("Energy Kcal", dietPlan.getEnergy_kcal(), 0d, 50d, context);
        ValidationUtil.validateDoubleLimits("Lipid", dietPlan.getLipid(), 0d, 50d, context);
        ValidationUtil.validateDoubleLimits("Protein", dietPlan.getProtein(), 0d, 50d, context);
        ValidationUtil.validateDoubleLimits("Carbohydrate", dietPlan.getCarbohydrate(), 0d, 50d, context);
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
