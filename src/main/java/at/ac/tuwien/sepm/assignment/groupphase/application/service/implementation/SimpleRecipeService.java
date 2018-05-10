package at.ac.tuwien.sepm.assignment.groupphase.application.service.implementation;

import at.ac.tuwien.sepm.assignment.groupphase.application.dto.Recipe;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.RecipePersistence;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.RecipeService;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.ServiceInvokationContext;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.ServiceInvokationException;
import at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation.ValidationUtil;
import org.springframework.stereotype.Service;

@Service
public class SimpleRecipeService implements RecipeService {
    private final RecipePersistence recipePersistence;

    public SimpleRecipeService(RecipePersistence recipePersistence) {
        this.recipePersistence = recipePersistence;
    }

    @Override
    public void create(Recipe r) throws ServiceInvokationException {
        ServiceInvokationContext context = new ServiceInvokationContext();
        if (!ValidationUtil.validateRecipe(r, context))
            throw new ServiceInvokationException(context);
        try {
            recipePersistence.create(r);
        } catch (PersistenceException e) {
            throw new ServiceInvokationException(e);
        }
    }
}