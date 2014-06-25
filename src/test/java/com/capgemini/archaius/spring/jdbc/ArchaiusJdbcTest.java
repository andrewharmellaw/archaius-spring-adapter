package com.capgemini.archaius.spring.jdbc;

import org.junit.After;
import org.junit.BeforeClass;

import com.capgemini.archaius.spring.jdbc.dataload.DeleteTestDataAndSchemaForArchaiusTest;
import com.capgemini.archaius.spring.jdbc.dataload.InitializingUserAuthenticationForEmbeddedDerbyDatabase;
import com.capgemini.archaius.spring.jdbc.dataload.LoadInitialArchaiusPropertyData;

public abstract class ArchaiusJdbcTest {

    @BeforeClass
    public static void setupClass() {

        InitializingUserAuthenticationForEmbeddedDerbyDatabase initialize = new InitializingUserAuthenticationForEmbeddedDerbyDatabase();
        initialize.initializedDerby();

        LoadInitialArchaiusPropertyData dataload = new LoadInitialArchaiusPropertyData();
        dataload.initializedDerby();
    }

    @After
    public void shutDownInmemoryDerbyDatabase() {
        DeleteTestDataAndSchemaForArchaiusTest deleteDB = new DeleteTestDataAndSchemaForArchaiusTest();
        deleteDB.deleteDatabase();
    }
}
