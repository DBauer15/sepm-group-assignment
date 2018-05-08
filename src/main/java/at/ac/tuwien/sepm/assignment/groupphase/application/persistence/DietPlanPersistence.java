package at.ac.tuwien.sepm.assignment.groupphase.application.persistence;

import at.ac.tuwien.sepm.assignment.groupphase.application.dto.DietPlan;

/**
 * Interface for Persistence layer of Diet Plan
 * @author e01529136
 *
 */
public interface DietPlanPersistence {

	public void create(DietPlan dietPlan) throws PersistenceException;
}
