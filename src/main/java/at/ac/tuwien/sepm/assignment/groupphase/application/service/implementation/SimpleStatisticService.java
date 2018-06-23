package at.ac.tuwien.sepm.assignment.groupphase.application.service.implementation;

import at.ac.tuwien.sepm.assignment.groupphase.application.dto.Recipe;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.StatisticPersistence;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.ServiceInvokationException;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.StatisticService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.Map;

@Service
public class SimpleStatisticService implements StatisticService {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private StatisticPersistence statisticPersistence;
    public SimpleStatisticService(StatisticPersistence statisticPersistence) {
        this.statisticPersistence = statisticPersistence;
    }

    @Override
    public Map<Recipe, Integer> getMostPopularRecipes() throws ServiceInvokationException {
        LOG.debug("Requested most popular recipes.");
        try {
            return statisticPersistence.getMostPopularRecipes();
        } catch (PersistenceException e) {
            throw new ServiceInvokationException(e);
        }
    }
}
