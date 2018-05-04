package at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public class JDBCConnectionManager {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private Connection connection;

    private static final String DB_NAME = "sepm_db";
    private static final String DB_USER = "user";
    private static final String DB_PASSWORD = "sepm";
    private static final String DB_SCRIPT = "classpath:sql/createAndInsert.sql";

    public Connection getConnection() throws SQLException {
        if (connection == null) {
            connection = DriverManager.getConnection("jdbc:h2:~/sepm_group_phase/db/" + DB_NAME + ";INIT=RUNSCRIPT FROM '" + DB_SCRIPT + "'",
                DB_USER,
                DB_PASSWORD);
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

}
