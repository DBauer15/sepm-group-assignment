package at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Test;

import at.ac.tuwien.sepm.assignment.groupphase.application.util.BaseTest;

public class JDBCConnectionManagerTest extends BaseTest {

	@Test
	public void testGetConnection_withTestDb_noExceptionAndNotNull() throws SQLException {
		Connection connection = JDBCConnectionManager.getConnection();

		Assert.assertNotNull(connection);
		JDBCConnectionManager.closeConnection();
	}

	@Test
	public void testTableIngredient_withTestDb_noExceptionAndAtLeastOneTupelInIngredientTable() throws SQLException {
		Connection connection = JDBCConnectionManager.getConnection();

		PreparedStatement getStmnt = connection.prepareStatement("select count(*) from INGREDIENT;");
		ResultSet resultSet = getStmnt.executeQuery();

		if (resultSet.next() == false) {
			Assert.fail("Result of 'select count(*) from INGREDIENT' should not be empty.");
		}
		Assert.assertTrue(resultSet.getInt(1) > 0);
		getStmnt.close();
		JDBCConnectionManager.closeConnection();
	}

}
