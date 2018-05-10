package at.ac.tuwien.sepm.assignment.groupphase.application.persistence;

import at.ac.tuwien.sepm.assignment.groupphase.application.dto.Recipe;

public interface RecipePersistence {
	/**
	 * Creates a new recipe with the supplied data in the storage.
	 * @param recipe {@link Recipe}
	 */
    void create(Recipe r) throws PersistenceException;
}