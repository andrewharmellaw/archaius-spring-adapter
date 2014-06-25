package com.capgemini.archaius.spring.util;


/**
 * Class which hold jdbc related contact values.
 * @author skumar81 
 */
public final class JdbcContants {
	
	private JdbcContants(){}

	/** The db url. */
	public static final String DB_URL ="dbURL";
	
	/** The driver class name. */
	public static final String DRIVER_CLASS_NAME="driverClassName";
	
	/** The username. */
	public static final String USERNAME="username";
	
	/** The password. */
	public static final String PASSWORD="password";
	
	/** The sql querry. */
	public static final String SQL_QUERRY="sqlQuerry";
	
	/** The key column name. */
	public static final String KEY_COLUMN_NAME="keyColumnName";
	
	/** The value column name. */
	public static final String VALUE_COLUMN_NAME="valueColumnName";
	
	/** The expected jdbc param count. */
	public static final int EXPECTED_JDBC_PARAM_COUNT=7;
	
	public static final String  IGNORE_RESOURCE_NOTFOUND="ignoreResourceNotFound";
	
	public static final String  IGNORE_DELETE_FROMSOURCE="ignoreDeletesFromSource";
	
	public static final String  INITIAL_DELAY_MILLIS="initialDelayMillis";
	
	public static final String  DELAY_MILLIS="delayMillis";
	
}
