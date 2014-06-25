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

public class ArchaiusPropertyDataLoader extends AbstractDerbyDriverLoader {

    public static Logger LOGGER = LoggerFactory.getLogger(ArchaiusPropertyDataLoader.class);

    public static void loadData() {

        loadDriver();

        Connection conn = null;

        ArrayList<Statement> statements = new ArrayList<>(); // list of Statements,
        PreparedStatement psInsert = null;
        Statement s = null;
        ResultSet rs = null;
        String dbName = "jdbcDemoDB"; // the name of the database

        try {

            Properties props = new Properties();
            props.put("user", "admin");
            props.put("password", "nimda");

            conn = DriverManager.getConnection(protocol + dbName + ";create=true", props);

            conn.setAutoCommit(false);

            s = conn.createStatement();
            statements.add(s);

            // We create a table...
            s.execute("create table MYSITEPROPERTIES(property_key varchar(40), property_value varchar(40))");
            LOGGER.info("Created table MySiteProperties");

            psInsert = conn.prepareStatement("insert into MYSITEPROPERTIES values (?, ?)");
            statements.add(psInsert);

            psInsert.setString(1, "Error404");
            psInsert.setString(2, "Page not found");
            psInsert.executeUpdate();
            LOGGER.info("Inserted Error404");

            psInsert.setString(1, "Error500");
            psInsert.setString(2, "Internal Server Error");
            psInsert.executeUpdate();
            LOGGER.info("Inserted Error500");

            rs = s.executeQuery("SELECT property_key, property_value FROM MYSITEPROPERTIES");

            while (rs.next()) {
                LOGGER.info("property_key : " + rs.getString(1));
                LOGGER.info("  and property_value : " + rs.getString(2));
            }

            conn.commit();
            LOGGER.info("Committed the transaction");
            
        } catch (SQLException sqle) {
            printSQLException(sqle);
        } finally {
			// release all open resources to avoid unnecessary memory usage

            // ResultSet
            try {
                if (rs != null) {
                    rs.close();
                    rs = null;
                }
            } catch (SQLException sqle) {
                printSQLException(sqle);
            }

            // Statements and PreparedStatements
            int i = 0;
            while (!statements.isEmpty()) {
                // PreparedStatement extend Statement
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

            // Connection
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
