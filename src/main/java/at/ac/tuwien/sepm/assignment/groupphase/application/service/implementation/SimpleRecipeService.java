package at.ac.tuwien.sepm.assignment.groupphase.application.service.implementation;

import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import at.ac.tuwien.sepm.assignment.groupphase.application.dto.Recipe;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.RecipePersistence;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.RecipeService;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.ServiceInvokationContext;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.ServiceInvokationException;
import at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation.RecipeValidator;

@Service
public class SimpleRecipeService implements RecipeService {

	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	private final RecipePersistence recipePersistence;
	private final RecipeValidator recipeValidator = new RecipeValidator();

	public SimpleRecipeService(RecipePersistence recipePersistence) {
		this.recipePersistence = recipePersistence;
	}

	@Override
	public void create(Recipe recipe) throws ServiceInvokationException {
		ServiceInvokationContext context = new ServiceInvokationContext();
		if (recipeValidator.validateForCreation(recipe, context) == false) {
			throw new ServiceInvokationException(context);
		}

		try {
			recipePersistence.create(recipe);
		} catch (PersistenceException e) {
			throw new ServiceInvokationException(e.getMessage());
		}
	}
}