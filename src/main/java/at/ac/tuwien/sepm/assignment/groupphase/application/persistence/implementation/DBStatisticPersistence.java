package at.ac.tuwien.sepm.assignment.groupphase.application.persistence.implementation;

import at.ac.tuwien.sepm.assignment.groupphase.application.dto.Recipe;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.RecipePersistence;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.StatisticPersistence;
import at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation.CloseUtil;
import at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation.JDBCConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.lang.invoke.MethodHandles;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Repository
public class DBStatisticPersistence implements StatisticPersistence {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final String SELECT_STATISTICS = "select recipe, count(recipe) as rc from DIET_PLAN_SUGGESTION where CREATED_TIMESTAMP in " +
        "(select max(CREATED_TIMESTAMP) from DIET_PLAN_SUGGESTION group by date, tag) group by recipe order by rc limit 10;";

    private RecipePersistence recipePersistence;

    public DBStatisticPersistence(RecipePersistence recipePersistence) {
        this.recipePersistence = recipePersistence;
    }

    @Override
    public Map<Recipe, Integer> getMostPopularRecipes() throws PersistenceException {
        LOG.debug("Fetching most popular recipes");
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = JDBCConnectionManager.getConnection().prepareStatement(SELECT_STATISTICS);
            rs = ps.executeQuery();

            Map<Recipe, Integer> mostPopular = new HashMap<>();
            while (rs.next())
                mostPopular.put(recipePersistence.get(rs.getInt("recipe")), rs.getInt("rc"));

            return mostPopular;
        } catch (SQLException e) {
            throw new PersistenceException(e);
        } finally {
            CloseUtil.closeStatement(ps);
            CloseUtil.closeResultSet(rs);
        }
    }
}
