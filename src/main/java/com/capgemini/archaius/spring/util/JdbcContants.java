package com.capgemini.archaius.spring.util;


/**
 * Class which hold jdbc related contact values.
 * @author skumar81 
 */
public final class JdbcContants {
	
	private JdbcContants(){}

	public static final String DB_URL ="dbURL";
	public static final String USERNAME="username";
	public static final String PASSWORD="password";
	public static final String SQL_QUERRY="sqlQuerry";
	public static final String KEY_COLUMN_NAME="keyColumnName";
	public static final String VALUE_COLUMN_NAME="valueColumnName";
	public static final int EXPECTED_JDBC_PARAM_COUNT=7;
	public static final String  IGNORE_RESOURCE_NOTFOUND="ignoreResourceNotFound";
	public static final String  IGNORE_DELETE_FROMSOURCE="ignoreDeletesFromSource";
	public static final String  INITIAL_DELAY_MILLIS="initialDelayMillis";
	public static final String  DELAY_MILLIS="delayMillis";	
}
