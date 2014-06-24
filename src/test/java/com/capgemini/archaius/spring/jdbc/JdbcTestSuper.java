package com.capgemini.archaius.spring.jdbc;

import com.capgemini.archaius.spring.jdbc.dataload.DeleteTestDataAndSchemaForArchaiusTest;
import org.junit.BeforeClass;

import com.capgemini.archaius.spring.jdbc.dataload.InitializingUserAuthenticationForEmbeddedDerbyDatabase;
import com.capgemini.archaius.spring.jdbc.dataload.LoadInitialArchaiusPropertyData;
import org.junit.After;

public class JdbcTestSuper {

    @BeforeClass
    public static void setupClass() {

        System.out.println("initialzing derby");
        InitializingUserAuthenticationForEmbeddedDerbyDatabase initialize = new InitializingUserAuthenticationForEmbeddedDerbyDatabase();
        initialize.initializedDerby();

        LoadInitialArchaiusPropertyData dataload = new LoadInitialArchaiusPropertyData();
        dataload.initializedDerby();

    }

    @After
    public void flushArchaiusData() {
        //shutting down the in memory database.
        DeleteTestDataAndSchemaForArchaiusTest deleteDB = new DeleteTestDataAndSchemaForArchaiusTest();
        deleteDB.deleteDatabase();
    }

}
