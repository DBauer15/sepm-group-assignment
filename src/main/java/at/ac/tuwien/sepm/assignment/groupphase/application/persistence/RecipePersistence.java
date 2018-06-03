package at.ac.tuwien.sepm.assignment.groupphase.application.persistence;

import java.util.List;

import at.ac.tuwien.sepm.assignment.groupphase.application.dto.IngredientSearchParam;
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
	 * @param searchParam {@link IngredientSearchParam}
	 * @return {@link List} of {@link RecipeIngredient}
	 * @throws PersistenceException if an persistence error occurs
	 */
	List<RecipeIngredient> searchIngredient(IngredientSearchParam searchParam) throws PersistenceException;

    /**
     * Fetches a recipe entry from the storage based on the given id.
     *
     * @param id An id for a storage entry
     * @return A recipe from the storage
     * @throws PersistenceException if any persistence errors occur or if no matching entry is found
     */
    Recipe get(int id) throws PersistenceException;
    
    /**
     * Deletes a recipe entry from the storage based on the given id.
     *
     * @param id An id for a storage entry
     * @throws PersistenceException if any persistence errors occur or if no matching entry is found
     */
    void delete(int id) throws PersistenceException;

    /**
     * Updates a recipe entry in the storage.
     *
     * @param recipe A recipe object with its unique id referencing an existing storage entry
     * @throws PersistenceException if any persistence errors occur
     */
    void update(Recipe recipe) throws PersistenceException;

    /**
     * Fetches all recipes from the storage.
     *
     * @return A list of all recipes
     * @throws PersistenceException if any persistence errors occur
     */
    List<Recipe> getRecipes() throws PersistenceException;
}