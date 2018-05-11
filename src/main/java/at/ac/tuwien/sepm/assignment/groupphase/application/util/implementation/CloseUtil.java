package at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.PersistenceException;

public class CloseUtil {
	private CloseUtil() {
	}

	public static void closeStatement(PreparedStatement s) throws PersistenceException {
		try {
			if (s != null && !s.isClosed())
				s.close();
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}

	public static void closeResultSet(ResultSet rs) throws PersistenceException {
		try {
			if (rs != null && !rs.isClosed()) {
				rs.close();
			}
		} catch (SQLException ex) {
			throw new PersistenceException(ex);
		}
	}
}