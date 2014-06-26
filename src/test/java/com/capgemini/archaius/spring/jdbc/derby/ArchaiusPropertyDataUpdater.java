package com.capgemini.archaius.spring.jdbc.derby;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class update the test data for Archaius to new value.
 * <UL>
 * <LI>Error500 = New Internal Server Error</LI>
 * <LI>Error404 = New Page not found Error400 = New Bad Request</LI>
 * </UL>
 *
 * @author Sanjay Kumar
 * @author Andrew Harmel-Law
 */
public class ArchaiusPropertyDataUpdater extends AbstractDerbyDriverLoader {

    public static Logger LOGGER = LoggerFactory.getLogger(ArchaiusPropertyDataUpdater.class);

    public static void updatePropertyData() {
        loadDriver();

        Connection conn = null;

        ArrayList<Statement> statements = new ArrayList<>(); // list of Statements,
        PreparedStatement psUpdate;
        Statement s;
        ResultSet rs = null;

        try {
            Properties props = new Properties(); // connection properties
            props.put("user", "admin");
            props.put("password", "nimda");

            String dbName = "jdbcDemoDB"; // the name of the database

            conn = DriverManager.getConnection(protocol + dbName + ";create=false", props);

            LOGGER.debug("Connected to and created database " + dbName);

            conn.setAutoCommit(false);

            s = conn.createStatement();
            statements.add(s);
            
            // Let
            psUpdate = conn.prepareStatement("update MYSITEPROPERTIES set property_key=?, property_value=? where property_key=?");
            statements.add(psUpdate);

            psUpdate.setString(1, "Error500");
            psUpdate.setString(2, "New Internal Server Error");
            psUpdate.setString(3, "Error500");
            psUpdate.executeUpdate();
            LOGGER.info("Updated Error500");

            psUpdate.setString(1, "Error404");
            psUpdate.setString(2, "New Page not found");
            psUpdate.setString(3, "Error404");
            psUpdate.executeUpdate();
            LOGGER.info("Updated Error404");

            conn.commit();
            
            LOGGER.info("Updated properties fresh from the database:");
            rs = s.executeQuery("SELECT property_key, property_value FROM MYSITEPROPERTIES");
            while (rs.next()) {
                LOGGER.info("property_key : " + rs.getString(1));
                LOGGER.info("and property_value : " + rs.getString(2));
            }
            conn.commit();

        } catch (SQLException sqle) {
            printSQLException(sqle);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                    rs = null;
                }
            } catch (SQLException sqle) {
                printSQLException(sqle);
            }

            int i = 0;
            while (!statements.isEmpty()) {
                Statement st = (Statement) statements.remove(i);
                try {
                    if (st != null) {
                        st.close();
                        st = null;
                    }
                } catch (SQLException sqle) {
                    printSQLException(sqle);
                }
            }

            try {
                if (conn != null) {
                    conn.close();
                    conn = null;
                }
            } catch (SQLException sqle) {
                printSQLException(sqle);
            }
        }
    }
}
