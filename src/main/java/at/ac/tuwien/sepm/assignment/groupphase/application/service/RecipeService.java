package at.ac.tuwien.sepm.assignment.groupphase.application.service;

import java.util.List;

import at.ac.tuwien.sepm.assignment.groupphase.application.dto.IngredientSearchParam;
import at.ac.tuwien.sepm.assignment.groupphase.application.dto.Recipe;
import at.ac.tuwien.sepm.assignment.groupphase.application.dto.RecipeIngredient;
import at.ac.tuwien.sepm.assignment.groupphase.application.dto.RecipeSearchParam;

/**
 * Service Interface for Recipes
 * @author e01529262
 *
 */
public interface RecipeService {
	/**
	 * Creates a new recipe with the supplied data.
	 * @param recipe {@link Recipe}
	 */
	public void create(Recipe recipe) throws ServiceInvokationException;
	
	/**
	 * Deletes a recipe.
     * @param id An id for a storage entry
	 */
	public void delete(int id) throws ServiceInvokationException;

	/**
	 * Search for an ingredient in the persistence layer.
	 * @param searchParam {@link IngredientSearchParam}
	 * @return {@link List} of {@link RecipeIngredient}
	 * @throws ServiceInvokationException if the supplied data is invalid
	 */
	public List<RecipeIngredient> searchIngredient(IngredientSearchParam searchParam) throws ServiceInvokationException;

    /**
     * Fetches a recipe entry from the persistence layer based on the given id.
     *
     * @param id An id for a storage entry
     * @return A recipe from the storage
     * @throws ServiceInvokationException if any errors occur in the persistence layer or if no matching entry is found
     */
    Recipe get(int id) throws ServiceInvokationException;

    /**
     * Updates a recipe entry in the persistence layer.
     *
     * @param recipe A recipe object with its unique id referencing an existing storage entry
     * @throws ServiceInvokationException if any persistence errors occur
     */
    void update(Recipe recipe) throws ServiceInvokationException;

    /**
     * Fetches all recipes from the storage.
     *
     * @return A list of all recipes
     * @throws ServiceInvokationException if any persistence errors occur
     */
    List<Recipe> getRecipes() throws ServiceInvokationException;
    
    /**
     * Search for Recipes that match the search criteria of the user.
     * @param searchParam {@link RecipeSearchParam} 
     * @return {@link List} of {@link Recipe}
     * @throws ServiceInvokationException if invalid data was supplied or an error in the persistence layer occured
     */
    public List<Recipe> searchRecipes(RecipeSearchParam searchParam) throws ServiceInvokationException;
}
