package at.ac.tuwien.sepm.assignment.groupphase.application.persistence;

import at.ac.tuwien.sepm.assignment.groupphase.application.dto.Recipe;

import java.util.List;

public interface RecipePersistence {
    Recipe get(int id) throws PersistenceException;

    void update(Recipe r) throws PersistenceException;

    List<Recipe> list() throws PersistenceException;
}
