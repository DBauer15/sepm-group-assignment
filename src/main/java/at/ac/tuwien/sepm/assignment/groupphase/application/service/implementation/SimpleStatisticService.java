package at.ac.tuwien.sepm.assignment.groupphase.application.service.implementation;

import at.ac.tuwien.sepm.assignment.groupphase.application.dto.Recipe;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.StatisticPersistence;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.ServiceInvokationException;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.StatisticService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SimpleStatisticService implements StatisticService {

    private StatisticPersistence statisticPersistence;
    public SimpleStatisticService(StatisticPersistence statisticPersistence) {
        this.statisticPersistence = statisticPersistence;
    }

    @Override
    public Map<Recipe, Integer> getMostPopularRecipes() throws ServiceInvokationException {
        try {
            return statisticPersistence.getMostPopularRecipes();
        } catch (PersistenceException e) {
            throw new ServiceInvokationException(e);
        }
    }
}
