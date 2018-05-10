package at.ac.tuwien.sepm.assignment.groupphase.application.service;

import at.ac.tuwien.sepm.assignment.groupphase.application.dto.Recipe;
import org.springframework.stereotype.Service;

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
}
