package at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation;

import java.io.IOException;
import java.io.InputStream;
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
			throw new PersistenceException(e.getMessage(), e);
		}
	}

	public static void closeResultSet(ResultSet rs) throws PersistenceException {
		try {
			if (rs != null && !rs.isClosed()) {
				rs.close();
			}
		} catch (SQLException ex) {
			throw new PersistenceException(ex.getMessage(), ex);
		}
	}

	public static void closeInputStream(InputStream is) throws PersistenceException {
		try {
			if (is != null) {
				is.close();
			}
		} catch (IOException ex) {
			throw new PersistenceException(ex.getMessage(), ex);
		}
	}
}