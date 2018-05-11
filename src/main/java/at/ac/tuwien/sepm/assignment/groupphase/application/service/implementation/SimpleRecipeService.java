package at.ac.tuwien.sepm.assignment.groupphase.application.service.implementation;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import at.ac.tuwien.sepm.assignment.groupphase.application.dto.IngredientSearchParam;
import at.ac.tuwien.sepm.assignment.groupphase.application.dto.Recipe;
import at.ac.tuwien.sepm.assignment.groupphase.application.dto.RecipeIngredient;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.RecipePersistence;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.RecipeService;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.ServiceInvokationContext;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.ServiceInvokationException;
import at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation.IngredientSearchParamValidator;
import at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation.RecipeValidator;
import at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation.ValidationUtil;

@Service
public class SimpleRecipeService implements RecipeService {

	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private final RecipePersistence recipePersistence;
	private final RecipeValidator recipeValidator = new RecipeValidator();
	private final IngredientSearchParamValidator ingredientSearchParamValidator = new IngredientSearchParamValidator();

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

	@Override
	public List<RecipeIngredient> searchIngredient(IngredientSearchParam searchParam)
			throws ServiceInvokationException {
		List<RecipeIngredient> searchResult = new ArrayList<>();
		ServiceInvokationContext context = new ServiceInvokationContext();

		if (ingredientSearchParamValidator.validateForReading(searchParam, context) == false) {
			throw new ServiceInvokationException(context);
		}
		try {
			searchResult = recipePersistence.searchIngredient(searchParam);
		} catch (PersistenceException e) {
			throw new ServiceInvokationException(e.getMessage());
		}

		return searchResult;
	}

    @Override
    public Recipe get(int id) throws ServiceInvokationException {
        try {
            return recipePersistence.get(id);
        } catch (PersistenceException e) {
            throw new ServiceInvokationException(e);
        }
    }

    @Override
    public void update(Recipe r) throws ServiceInvokationException {
        ServiceInvokationContext context = new ServiceInvokationContext();
        if (!recipeValidator.validateForUpdate(r, context))
            throw new ServiceInvokationException(context);
        try {
            recipePersistence.update(r);
        } catch (PersistenceException e) {
            throw new ServiceInvokationException(e);
        }
    }

    @Override
    public List<Recipe> getRecipes() throws ServiceInvokationException {
        try {
            return recipePersistence.getRecipes();
        } catch (PersistenceException e) {
            throw new ServiceInvokationException(e);
        }
    }
}