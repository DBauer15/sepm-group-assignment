package at.ac.tuwien.sepm.assignment.groupphase.application.persistence.implementation;

import at.ac.tuwien.sepm.assignment.groupphase.application.dto.Recipe;
import at.ac.tuwien.sepm.assignment.groupphase.application.dto.RecipeIngredient;
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
import java.util.ArrayList;
import java.util.List;

@Repository
public class DBRecipePersistence implements RecipePersistence {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final String SELECT_RECIPE_WHERE = "SELECT * FROM RECIPE WHERE ID = ?;";
    private static final String SELECT_INGREDIENTS_WHERE = "SELECT * FROM RECIPE_INGREDIENT r_i JOIN INGREDIENT i ON r_i.INGREDIENT_ID = i.ID JOIN RECIPE r ON r_i.RECIPE_ID = r.ID WHERE r.ID = ?;";
    private static final String UPDATE_RECIPE_WHERE = "UPDATE RECIPE SET NAME = ?, DURATION = ?, DESCRIPTION = ?, TAGS = ?, DELETED = ? WHERE ID = ?;";

    @Override
    public Recipe get(int id) throws PersistenceException {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = JDBCConnectionManager.getConnection().prepareStatement(SELECT_RECIPE_WHERE);
            ps.setInt(1, id);

            rs = ps.executeQuery();

            if (rs.next()) {
                Recipe r = new Recipe(
                    rs.getInt("ID"),
                    rs.getString("NAME"),
                    rs.getDouble("DURATION"),
                    rs.getString("DESCRIPTION"),
                    rs.getString("TAGS"),
                    rs.getBoolean("DELETED"));
                r.setRecipeIngredients(getIngredients(id));
                return r;
            }

            throw new PersistenceException("No recipe found for given id");
        } catch (SQLException e) {
            throw new PersistenceException(e);
        } finally {
            CloseUtil.closeStatement(ps);
            CloseUtil.closeResultSet(rs);
        }
    }

    private List<RecipeIngredient> getIngredients(int id) throws PersistenceException {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = JDBCConnectionManager.getConnection().prepareStatement(SELECT_INGREDIENTS_WHERE);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            List<RecipeIngredient> ingredients = new ArrayList<>();
            while (rs.next()) {
                ingredients.add(new RecipeIngredient(
                    rs.getInt("INGREDIENT_ID"),
                    rs.getDouble("AMOUNT"),
                    rs.getDouble("ENERG_KCAL"),
                    rs.getDouble("LIPID"),
                    rs.getDouble("PROTEIN"),
                    rs.getDouble("CARBOHYDRT"),
                    rs.getString("UNIT_NAME"),
                    rs.getDouble("UNIT_GRAM_NORMALISED"),
                    rs.getBoolean("USER_SPECIFIC"),
                    rs.getString("NAME")
                ));
            }

            return ingredients;
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
            ps = JDBCConnectionManager.getConnection().prepareStatement(UPDATE_RECIPE_WHERE);
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

