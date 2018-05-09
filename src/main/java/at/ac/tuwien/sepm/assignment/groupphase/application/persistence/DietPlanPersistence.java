package at.ac.tuwien.sepm.assignment.groupphase.application.persistence;

import at.ac.tuwien.sepm.assignment.groupphase.application.dto.DietPlan;

/**
 * Interface for Persistence layer of Diet Plan
 * @author e01529136
 *
 */
public interface DietPlanPersistence {

	public void create(DietPlan dietPlan) throws PersistenceException;

    /**
     * Switches to the given Diet Plan and activates it. The currently selected plan will be deactivated
     * @param dietPlan {@link DietPlan}
     * @throws PersistenceException
     */
	public void switchTo(DietPlan dietPlan) throws PersistenceException;
}
