package com.capgemini.archaius.spring.jdbc;

import org.junit.BeforeClass;

import com.capgemini.archaius.spring.jdbc.dataload.InitializingUserAuthenticationForEmbeddedDerbyDatabase;
import com.capgemini.archaius.spring.jdbc.dataload.LoadInitialArchaiusPropertyData;


public class JdbcTestSuper {

	@BeforeClass
	public static void setupClass(){
		
		System.out.println("initialzing derby");
		InitializingUserAuthenticationForEmbeddedDerbyDatabase initialize=new InitializingUserAuthenticationForEmbeddedDerbyDatabase();
		initialize.initializedDerby();
		
		LoadInitialArchaiusPropertyData dataload = new LoadInitialArchaiusPropertyData();
		dataload.initializedDerby();
		
	}
}
