package com.capgemini.archaius.spring.jdbc.derby;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmbeddedDerbyUserAuthenticationActivator extends AbstractDerbyDriverLoader {

    public static Logger LOGGER = LoggerFactory.getLogger(EmbeddedDerbyUserAuthenticationActivator.class);

    public static void activateAuthentication() {

        loadDriver();

        Connection conn = null;

        String dbName = "jdbcDemoDB"; // the name of the database

        String connectionURL = protocol + dbName + ";create=true";

        // Start the database and set up users, then close database
        try {
            LOGGER.info("Trying to connect to url : {}", connectionURL);
            conn = DriverManager.getConnection(connectionURL);
            LOGGER.info("Connected to database: {} ", connectionURL);

            turnOnBuiltInUsers(conn);

            // shut down the database
            conn.close();
            LOGGER.info("Closed connection");

            // so Derby can be restarted
            System.gc();
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    /**
     * Turn on built-in user authentication and user authorization.
     *
     * @param conn a connection to the database.
     */
    private static void turnOnBuiltInUsers(Connection conn) throws SQLException {
        LOGGER.info("Turning on authentication.");
        Statement s = conn.createStatement();

        // Setting and Confirming requireAuthentication
        s.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY("
                + "'derby.connection.requireAuthentication', 'true')");
        ResultSet rs = s
                .executeQuery("VALUES SYSCS_UTIL.SYSCS_GET_DATABASE_PROPERTY("
                        + "'derby.connection.requireAuthentication')");
        rs.next();
        LOGGER.info("Value of requireAuthentication is "
                + rs.getString(1));
        // Setting authentication scheme to Derby
        s.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY("
                + "'derby.authentication.provider', 'BUILTIN')");

        // Creating some sample users
        s.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY("
                + "'derby.user.admin', 'nimda')");
        s.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY("
                + "'derby.user.guest', 'guest')");
        s.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY("
                + "'derby.user.test', 'test')");

		// Setting default connection mode to no access
        // (user authorization)
        s.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY("
                + "'derby.database.defaultConnectionMode', 'noAccess')");
        // Confirming default connection mode
        rs = s.executeQuery("VALUES SYSCS_UTIL.SYSCS_GET_DATABASE_PROPERTY("
                + "'derby.database.defaultConnectionMode')");
        rs.next();
        LOGGER.info("Value of defaultConnectionMode is "
                + rs.getString(1));

        // Defining read-write users
        s.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY("
                + "'derby.database.fullAccessUsers', 'admin')");

        // Defining read-only users
        s.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY("
                + "'derby.database.readOnlyAccessUsers', 'guest,test')");

        // Confirming full-access users
        rs = s.executeQuery("VALUES SYSCS_UTIL.SYSCS_GET_DATABASE_PROPERTY("
                + "'derby.database.fullAccessUsers')");
        rs.next();
        LOGGER.info("Value of fullAccessUsers is " + rs.getString(1));

        // Confirming read-only users
        rs = s.executeQuery("VALUES SYSCS_UTIL.SYSCS_GET_DATABASE_PROPERTY("
                + "'derby.database.readOnlyAccessUsers')");
        rs.next();
        System.out
                .println("Value of readOnlyAccessUsers is " + rs.getString(1));

		// We would set the following property to TRUE only
        // when we were ready to deploy.
        s.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY("
                + "'derby.database.propertiesOnly', 'false')");
        s.close();
    }

}
