package at.ac.tuwien.sepm.assignment.groupphase.application.service.implementation;

import at.ac.tuwien.sepm.assignment.groupphase.application.dto.Recipe;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.RecipePersistence;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.RecipeService;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.ServiceInvokationContext;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.ServiceInvokationException;
import at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation.RecipeValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Service
public class SimpleRecipeService implements RecipeService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final RecipePersistence recipePersistence;
    private final RecipeValidator recipeValidator = new RecipeValidator();

    public SimpleRecipeService(RecipePersistence recipePersistence) {
        this.recipePersistence = recipePersistence;
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
    public List<Recipe> list() throws ServiceInvokationException {
        try {
            return recipePersistence.list();
        } catch (PersistenceException e) {
            throw new ServiceInvokationException(e);
        }
    }
}
