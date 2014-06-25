package com.capgemini.archaius.spring.jdbc.derby;

import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Sanjay Kumar.
 */
public class ArchaiusTestDataAndSchemaDestroyer extends AbstractDerbyDriverLoader {

    public static Logger LOGGER = LoggerFactory.getLogger(ArchaiusTestDataAndSchemaDestroyer.class);

    public static void deleteDatabase() {

        loadDriver();

        try {
            LOGGER.info("Going to issue the shutdown of DB");
            DriverManager.getConnection("jdbc:derby:memory:jdbcDemoDB;user=admin;password=nimda;shutdown=true");

        } catch (SQLException se) {
            if (((se.getErrorCode() == 50000) && ("XJ015".equals(se
                    .getSQLState())))) {
                // we got the expected exception
                LOGGER.info("Derby shut down normally");
            }
            if (((se.getErrorCode() == 45000) && ("08006".equals(se
                    .getSQLState())))) {
                // we got the expected exception
                LOGGER.info("Derby shut down normally");
                LOGGER.info("going to drop table");
                try {
                    DriverManager
                            .getConnection("jdbc:derby:memory:jdbcDemoDB;user=admin;password=nimda;drop=true");
                } catch (SQLException e) {
                    if (((e.getErrorCode() == 45000) && ("08006".equals(e
                            .getSQLState())))) {
                        // we got the expected exception
                        LOGGER.info("Derby dropped table normally");
                    } else {
                        // if the error code or SQLState is different, we have
                        // an unexpected exception (shutdown failed)
                        LOGGER.error("Derby did not shut down normally");
                        printSQLException(se);
                    }
                }
                LOGGER.info("Derby dropped table normally");
            } else {
                // if the error code or SQLState is different, we have
                // an unexpected exception (shutdown failed)
                LOGGER.error("Derby did not shut down normally");
                printSQLException(se);
            }
        }
    }
}
