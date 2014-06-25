package com.capgemini.archaius.spring.jdbc.derby;

import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author skumar81
 */
public abstract class AbstractDerbyDriverLoader {
    
    public static Logger LOGGER = LoggerFactory.getLogger(AbstractDerbyDriverLoader.class);
    protected static String driver = "org.apache.derby.jdbc.EmbeddedDriver";
    protected static String protocol = "jdbc:derby:memory:";

    protected static void loadDriver() {
        try {
            Class.forName(driver).newInstance();
            LOGGER.info("Loaded the appropriate driver");
        } catch (ClassNotFoundException cnfe) {
            LOGGER.error("\nUnable to load the JDBC driver " + driver);
            LOGGER.error("Please check your CLASSPATH.");
            cnfe.printStackTrace(System.err);
        } catch (InstantiationException ie) {
            LOGGER.error("\nUnable to instantiate the JDBC driver " + driver);
            ie.printStackTrace(System.err);
        } catch (IllegalAccessException iae) {
            LOGGER.error("\nNot allowed to access the JDBC driver " + driver);
            iae.printStackTrace(System.err);
        }
    }

    public static void printSQLException(SQLException e) {
        while (e != null) {
            LOGGER.error("\n----- SQLException -----");
            LOGGER.error("  SQL State:  " + e.getSQLState());
            LOGGER.error("  Error Code: " + e.getErrorCode());
            LOGGER.error("  Message:    " + e.getMessage());
            // for stack traces, refer to derby.log or uncomment this:
            // e.printStackTrace(System.err);
            e = e.getNextException();
        }
    }
}
