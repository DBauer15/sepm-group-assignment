package at.ac.tuwien.sepm.assignment.groupphase.application.persistence.implementation;

import at.ac.tuwien.sepm.assignment.groupphase.application.dto.Recipe;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.RecipePersistence;
import at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation.CloseUtil;
import at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation.JDBCConnectionManager;
import org.springframework.stereotype.Repository;

import java.sql.*;

@Repository
public class DBRecipePersistence implements RecipePersistence {
	private static final String CREATE_RECIPE = "INSERT INTO RECIPE (name, duration, description, tags, deleted) VALUES (?, ?, ?, ?, ?)";

	private PreparedStatement ps;

	@Override
	public void create(Recipe recipe) throws PersistenceException {
		ResultSet generatedKeys = null;
		int i = 1;

		try {
			Connection connection = JDBCConnectionManager.getConnection();
			ps = connection.prepareStatement(CREATE_RECIPE, Statement.RETURN_GENERATED_KEYS);
			ps.setString(i++, recipe.getName());
			ps.setDouble(i++, recipe.getDuration());

			Clob clob = connection.createClob();
			clob.setString(1, recipe.getDescription());

			ps.setClob(i++, clob);
			ps.setString(i++, recipe.getTagsAsString());
			ps.setBoolean(i++, false);
			ps.execute();

			generatedKeys = ps.getGeneratedKeys();
			generatedKeys.next();

			recipe.setId(generatedKeys.getInt(1));
		} catch (SQLException e) {
			throw new PersistenceException(
					"There was an error while creating a recipe in the database. " + e.getMessage());
		} finally {
			CloseUtil.closeResultSet(generatedKeys);
			CloseUtil.closeStatement(ps);
		}
	}
}