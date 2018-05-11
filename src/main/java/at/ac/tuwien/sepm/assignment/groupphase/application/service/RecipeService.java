package at.ac.tuwien.sepm.assignment.groupphase.application.service;

import java.util.List;

import at.ac.tuwien.sepm.assignment.groupphase.application.dto.IngredientSearchParam;
import at.ac.tuwien.sepm.assignment.groupphase.application.dto.Recipe;
import at.ac.tuwien.sepm.assignment.groupphase.application.dto.RecipeIngredient;

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
	 * Search for an ingredient in the persistence layer.
	 * @param searchParam {@link IngredientSearchParam}
	 * @return {@link List} of {@link RecipeIngredient}
	 * @throws ServiceInvokationException if the supplied data is invalid
	 */
	public List<RecipeIngredient> searchIngredient(IngredientSearchParam searchParam) throws ServiceInvokationException;
}
