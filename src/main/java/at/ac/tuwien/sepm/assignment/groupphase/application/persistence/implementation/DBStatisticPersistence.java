package at.ac.tuwien.sepm.assignment.groupphase.application.persistence.implementation;

import at.ac.tuwien.sepm.assignment.groupphase.application.dto.Recipe;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.StatisticPersistence;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class DBStatisticPersistence implements StatisticPersistence {
    @Override
    public Map<Recipe, Integer> getMostPopularRecipes() throws PersistenceException {
        //TODO
        return null;
    }
}
