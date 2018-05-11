package at.ac.tuwien.sepm.assignment.groupphase.application.util;

import java.lang.invoke.MethodHandles;

import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation.JDBCConnectionManager;

public class BaseTest {
	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	/**
	 * Set System Properties for Test Execution.
	 * Whether or not to use the test database and if database access should be logged.
	 */
	private void setSystemPropertiesForTests() {
		LOG.info("Setting System Properties - before test method invokation.");
		System.setProperty("at.ac.tuwien.sepm.testmode", "true");
		System.setProperty("at.ac.tuwien.sepm.db_log", "true");
	}

	@Before
	public void beforeMethod() {
		setSystemPropertiesForTests();
		LOG.info("Closing JDBC Connection - before test method invokation.");
		JDBCConnectionManager.closeConnection();
	}

}
