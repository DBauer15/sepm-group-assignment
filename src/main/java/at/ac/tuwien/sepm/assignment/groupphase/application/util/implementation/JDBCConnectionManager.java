package at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public class JDBCConnectionManager {

	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private Connection connection;

	private static final String SYS_PROPERTY_TESTMODE = "at.ac.tuwien.sepm.testmode";
	private static final String SYS_PROPERTY_DB_LOG = "at.ac.tuwien.sepm.db_log";

	private static final String DB_FILE_TEST = "src/test/resources/db/sepm_test_db";
	private static final String DB_FILE = "src/main/resources/db/sepm_db";

	private static final String DB_USER = "user";
	private static final String DB_PASSWORD = "sepm";

	private static final String TRACE_LEVEL = "TRACE_LEVEL_FILE=4"; // log h2 database output via slf4j

	private static final String INIT_SCRIPT_TEST = ""; // TODO for test "INIT=runscript from
														// 'classpath:createAndInsert.sql'";
	private static final String INIT_SCRIPT = "";

	public Connection getConnection() throws SQLException {
		if (connection == null) {
			connection = DriverManager.getConnection(
					String.format("%s;%s;%s", getDbUrl(), getInitScript(), getTraceLevel()), DB_USER, DB_PASSWORD);
		}
		return connection;
	}

	public void closeConnection() {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				LOG.error("Failed to close connection '{}'", e.getMessage(), e);
			}
			connection = null;
		}
	}

	private static String getTraceLevel() {
		return isSysPropertySet(SYS_PROPERTY_DB_LOG, "true") ? TRACE_LEVEL : "";
	}

	private static String getInitScript() {
		return isSysPropertySet(SYS_PROPERTY_TESTMODE, "true") ? INIT_SCRIPT_TEST : INIT_SCRIPT;
	}

	private static String getDbUrl() {
		return isSysPropertySet(SYS_PROPERTY_TESTMODE, "true")
				? String.format("jdbc:h2:file:%s", new File(DB_FILE_TEST).getAbsolutePath())
				: String.format("jdbc:h2:file:%s", new File(DB_FILE).getAbsolutePath());
	}

	private static boolean isSysPropertySet(String propertyName, String propertyValue) {
		return propertyValue.equals(System.getProperty(propertyName));
	}

}
