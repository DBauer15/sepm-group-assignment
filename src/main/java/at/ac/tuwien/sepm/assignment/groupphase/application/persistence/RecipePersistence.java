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

    public Recipe get(int id) throws PersistenceException;

    public void update(Recipe recipe) throws PersistenceException;

    public List<Recipe> list() throws PersistenceException;
}