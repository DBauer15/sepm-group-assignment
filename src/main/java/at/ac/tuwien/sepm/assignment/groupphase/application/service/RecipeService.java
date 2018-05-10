package at.ac.tuwien.sepm.assignment.groupphase.application.service;

import at.ac.tuwien.sepm.assignment.groupphase.application.dto.Recipe;

import java.util.List;

public interface RecipeService {
    Recipe get(int id) throws ServiceInvokationException;

    void update(Recipe r) throws ServiceInvokationException;

    List<Recipe> list();
}
