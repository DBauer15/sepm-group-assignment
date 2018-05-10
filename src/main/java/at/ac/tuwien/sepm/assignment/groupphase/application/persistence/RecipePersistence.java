package at.ac.tuwien.sepm.assignment.groupphase.application.persistence;

import java.util.List;

import at.ac.tuwien.sepm.assignment.groupphase.application.dto.Recipe;
import at.ac.tuwien.sepm.assignment.groupphase.application.dto.RecipeIngredient;

public interface RecipePersistence {
	/**
	 * Creates a new recipe with the supplied data in the storage.
	 * @param recipe {@link Recipe}
	 * @throws PersistenceException if an persistence error occurs
	 */
	void create(Recipe r) throws PersistenceException;

	/**
	 * Search for an ingredient and returns a list of matching recipe ingredients.
	 * Note: field 'amount' of {@link RecipeIngredient} cannot be initalised and is therefore <code>null</code>
	 * @param query {@link String}
	 * @return {@link List} of {@link RecipeIngredient}
	 * @throws PersistenceException if an persistence error occurs
	 */
	List<RecipeIngredient> searchIngredient(String query) throws PersistenceException;
}