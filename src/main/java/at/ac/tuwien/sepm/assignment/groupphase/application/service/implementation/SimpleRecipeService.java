package at.ac.tuwien.sepm.assignment.groupphase.application.service.implementation;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

import at.ac.tuwien.sepm.assignment.groupphase.application.util.Validator;
import at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation.NutritionUtil;
import at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation.ValidationUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import at.ac.tuwien.sepm.assignment.groupphase.application.dto.IngredientSearchParam;
import at.ac.tuwien.sepm.assignment.groupphase.application.dto.Recipe;
import at.ac.tuwien.sepm.assignment.groupphase.application.dto.RecipeIngredient;
import at.ac.tuwien.sepm.assignment.groupphase.application.dto.RecipeSearchParam;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.RecipePersistence;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.RecipeService;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.ServiceInvokationContext;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.ServiceInvokationException;

@Service
public class SimpleRecipeService implements RecipeService {

	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private final RecipePersistence recipePersistence;
	private final Validator<Recipe> recipeValidator;
	private final Validator<IngredientSearchParam> ingredientSearchParamValidator;

	public SimpleRecipeService(RecipePersistence recipePersistence, Validator<Recipe> recipeValidator, Validator<IngredientSearchParam> ingredientSearchParamValidator) {
		this.recipePersistence = recipePersistence;
		this.recipeValidator = recipeValidator;
		this.ingredientSearchParamValidator = ingredientSearchParamValidator;
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
			throw new ServiceInvokationException(e.getMessage(), e);
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
			throw new ServiceInvokationException(e.getMessage(), e);
		}

		return searchResult;
	}

	@Override
	public Recipe get(int id) throws ServiceInvokationException {
		try {
			Recipe r = recipePersistence.get(id);
			r = NutritionUtil.fillNutritionValues(r);

			ServiceInvokationContext context = new ServiceInvokationContext();
			if (!recipeValidator.validateForReading(r, context))
				throw new ServiceInvokationException(context);

			return r;
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
			List<Recipe> recipes = recipePersistence.getRecipes();
			recipes.forEach(NutritionUtil::fillNutritionValues);

			ServiceInvokationContext context = new ServiceInvokationContext();
			for (Recipe r : recipes)
				if (!recipeValidator.validateForReading(r, context))
					throw new ServiceInvokationException(context);

			return recipes;
		} catch (PersistenceException e) {
			throw new ServiceInvokationException(e);
		}
	}

	@Override
	public List<Recipe> searchRecipes(RecipeSearchParam searchParam) throws ServiceInvokationException {

		ServiceInvokationContext context = new ServiceInvokationContext();
		if (ValidationUtil.validateRecipeSearchParam(searchParam, context) == false) {
			throw new ServiceInvokationException(context);
		}

		try {
			List<Recipe> recipes = recipePersistence.searchRecipes(searchParam);
			recipes.forEach(NutritionUtil::fillNutritionValues);

			context = new ServiceInvokationContext();
			for (Recipe r : recipes)
				if (!recipeValidator.validateForReading(r, context))
					throw new ServiceInvokationException(context);

			return recipes;
		} catch (PersistenceException e) {
			throw new ServiceInvokationException(e);
		}

	}

	@Override
	public void delete(int id) throws ServiceInvokationException {
		try {
			recipePersistence.delete(id);
		} catch (PersistenceException e) {
			throw new ServiceInvokationException(e);
		}
	}

}