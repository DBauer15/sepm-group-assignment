package at.ac.tuwien.sepm.assignment.groupphase.application.persistence.implementation;

import at.ac.tuwien.sepm.assignment.groupphase.application.dto.Recipe;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.RecipePersistence;
import at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation.JDBCConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.lang.invoke.MethodHandles;
import java.sql.*;
import java.util.List;

@Repository
public class DBRecipePersistence implements RecipePersistence {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final String SELECT_RECIPE = "SELECT * FROM RECIPE WHERE ID = ?;";
    private static final String UPDATE_RECIPE = "UPDATE RECIPE SET NAME = ?, DURATION = ?, DESCRIPTION = ?, TAGS = ?, DELETED = ? WHERE ID = ?";

    private final Connection connection;
    private PreparedStatement ps;

    public DBRecipePersistence(JDBCConnectionManager jdbcConnectionManager) throws PersistenceException {
        try {
            connection = jdbcConnectionManager.getConnection();
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public Recipe get(int id) throws PersistenceException {
        try {
            ps = connection.prepareStatement(SELECT_RECIPE);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next())
                return new Recipe(rs.getInt(1), rs.getString(2), rs.getDouble(3), rs.getString(5), rs.getBoolean(6));

            throw new PersistenceException("No recipe found for given id");
        } catch (SQLException e) {
            throw new PersistenceException(e);
        } finally {
            closeStatement(ps);
        }
    }

    @Override
    public void update(Recipe recipe) throws PersistenceException {
        try {
            ps = connection.prepareStatement(UPDATE_RECIPE);
            ps.setString(1, recipe.getName());
            ps.setDouble(2, recipe.getDuration());
            //TODO what is Clob?
            ps.setClob(3, (Clob) null);
            ps.setString(4, recipe.getTagsAsString());
            ps.setBoolean(5, recipe.getDeleted());
            ps.setInt(6, recipe.getId());
            ps.executeQuery();
        } catch (SQLException e) {
            throw new PersistenceException(e);
        } finally {
            closeStatement(ps);
        }
    }

    @Override
    public List<Recipe> list() {
        return null;
    }

    private void closeStatement(PreparedStatement s) throws PersistenceException {
        try {
            if (s != null && !s.isClosed())
                s.close();
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }
}

