package at.ac.tuwien.sepm.assignment.groupphase.application.service;

import at.ac.tuwien.sepm.assignment.groupphase.application.dto.DietPlan;
import at.ac.tuwien.sepm.assignment.groupphase.application.dto.Recipe;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.NoEntryFoundException;

import java.util.List;

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
     * Reads all available diet plans.
     * @return {@link List<DietPlan>} of all diet plans
     * @throws ServiceInvokationException
     */
	public List<DietPlan> readAll() throws ServiceInvokationException;

    /**
     * Reads the active diet plan.
     * @return Currently active {@link DietPlan}
     * @throws ServiceInvokationException
     * @throws NoEntryFoundException Thrown when there is no active diet plan selected
     */
	public DietPlan readActive() throws ServiceInvokationException, NoEntryFoundException;

    /**
     * Switches to the given Diet Plan and activates it. The currently selected plan will be deactivated
     * @param dietPlan {@link DietPlan}
     * @throws ServiceInvokationException
     */
	public void switchTo(DietPlan dietPlan) throws ServiceInvokationException;

    /**
     * Updates a custom diet plan entry in the persistence layer.
     *
     * @param dietPlan A DietPlan object with its unique id referencing an existing storage entry
     * @throws ServiceInvokationException if any persistence errors occur
     */
    void update(DietPlan dietPlan) throws ServiceInvokationException;
}
