package at.ac.tuwien.sepm.assignment.groupphase.application.persistence;

import at.ac.tuwien.sepm.assignment.groupphase.application.dto.DietPlan;

import java.util.List;

/**
 * Interface for Persistence layer of Diet Plan
 * @author e01529136
 *
 */
public interface DietPlanPersistence {

    /**
     * Creates a new Diet Plan with the supplied data. The diet plan will not be set active.
     * @param dietPlan {@link DietPlan}
     * @throws PersistenceException
     */
	public void create(DietPlan dietPlan) throws PersistenceException;

    /**
     * Reads all available diet plans.
     * @return {@link List<DietPlan>} of all diet plans
     * @throws PersistenceException
     */
	public List<DietPlan> readAll() throws PersistenceException;

    /**
     * Reads the active diet plan.
     * @return Currently active {@link DietPlan}
     * @throws PersistenceException
     */
	public DietPlan readActive() throws PersistenceException, NoEntryFoundException;

    /**
     * Switches to the given Diet Plan and activates it. The currently selected plan will be deactivated
     * @param dietPlan {@link DietPlan}
     * @throws PersistenceException
     */
	public void switchTo(DietPlan dietPlan) throws PersistenceException;
}
