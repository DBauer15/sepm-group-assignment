package at.ac.tuwien.sepm.assignment.groupphase.application.service;

import at.ac.tuwien.sepm.assignment.groupphase.application.dto.DietPlan;

/**
 * Service Interface for Diet Plan
 * @author e01529136
 *
 */
public interface DietPlanService {

	/**
	 * Creates a new Diet Plan with the supplied data. The diet plan will not be set active.
	 * @param dietPlan {@link DietPlan}
     * @throws ServiceInvokationException
	 */
	public void create(DietPlan dietPlan) throws ServiceInvokationException;

    /**
     * Switches to the given Diet Plan and activates it. The currently selected plan will be deactivated
     * @param dietPlan {@link DietPlan}
     * @throws ServiceInvokationException
     */
	public void switchTo(DietPlan dietPlan) throws ServiceInvokationException;
}
