package at.ac.tuwien.sepm.assignment.groupphase.application.util;

import org.junit.Before;

public class BaseTest {
	private static String currWorkingDir = null;

	@Before
	/**
	 * Set System Properties for Test Execution.
	 * Whether or not to use the test database and if database access should be logged.
	 */
	public void setSystemPropertiesForTests() {
		System.setProperty("at.ac.tuwien.sepm.testmode", "true");
		System.setProperty("at.ac.tuwien.sepm.db_log", "true");
	}

	protected static String getCurrWorkingDir() {
		if (currWorkingDir == null) {
			currWorkingDir = System.getProperty("user.dir");
		}
		return currWorkingDir;
	}
}
