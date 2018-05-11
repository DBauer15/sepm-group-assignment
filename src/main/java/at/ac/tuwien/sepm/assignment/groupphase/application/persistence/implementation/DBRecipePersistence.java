package at.ac.tuwien.sepm.assignment.groupphase.application.persistence.implementation;

import at.ac.tuwien.sepm.assignment.groupphase.application.dto.Recipe;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.RecipePersistence;
import at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation.CloseUtil;
import at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation.JDBCConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.lang.invoke.MethodHandles;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class DBRecipePersistence implements RecipePersistence {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final String SELECT_RECIPE = "SELECT * FROM RECIPE WHERE ID = ?;";
    private static final String UPDATE_RECIPE = "UPDATE RECIPE SET NAME = ?, DURATION = ?, DESCRIPTION = ?, TAGS = ?, DELETED = ? WHERE ID = ?";

    @Override
    public Recipe get(int id) throws PersistenceException {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = JDBCConnectionManager.getConnection().prepareStatement(SELECT_RECIPE);
            ps.setInt(1, id);

            rs = ps.executeQuery();

            if (rs.next())
                return new Recipe(rs.getInt(1), rs.getString(2), rs.getDouble(3), rs.getString(4), rs.getString(5), rs.getBoolean(6));

            throw new PersistenceException("No recipe found for given id");
        } catch (SQLException e) {
            throw new PersistenceException(e);
        } finally {
            CloseUtil.closeStatement(ps);
            CloseUtil.closeResultSet(rs);
        }
    }

    @Override
    public void update(Recipe recipe) throws PersistenceException {
        PreparedStatement ps = null;
        try {
            ps = JDBCConnectionManager.getConnection().prepareStatement(UPDATE_RECIPE);
            ps.setString(1, recipe.getName());
            ps.setDouble(2, recipe.getDuration());

            Clob description = JDBCConnectionManager.getConnection().createClob();
            description.setString(1, recipe.getDescription());
            ps.setClob(3, description);

            ps.setString(4, recipe.getTagsAsString());
            ps.setBoolean(5, recipe.getDeleted());
            ps.setInt(6, recipe.getId());
            ps.executeQuery();
        } catch (SQLException e) {
            throw new PersistenceException(e);
        } finally {
            CloseUtil.closeStatement(ps);
        }
    }

    @Override
    public List<Recipe> list() {
        return null;
    }
}

