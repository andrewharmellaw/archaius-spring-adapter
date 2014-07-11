/*
 * Copyright (C) 2014 Capgemini (oss@capgemini.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.capgemini.archaius.spring;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.netflix.config.ConcurrentCompositeConfiguration;
import com.netflix.config.DynamicConfiguration;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.DynamicURLConfiguration;
import com.netflix.config.FixedDelayPollingScheduler;
import com.netflix.config.sources.JDBCConfigurationSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.capgemini.archaius.spring.constants.JdbcContants;

/**
 * This class builds the property configuration factory for the location(s)
 * provided.
 *
 * @author Andrew Harmel-Law
 * @author Nick Walter
 * @author Sanjay Kumar
 */
class ArchaiusSpringPropertyPlaceholderSupport {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArchaiusSpringPropertyPlaceholderSupport.class);

    protected String resolvePlaceholder(String placeholder, Properties props, int systemPropertiesMode) {
        return DynamicPropertyFactory.getInstance().getStringProperty(placeholder, null).get();
    }

    protected void setLocations(Resource[] locations,
            boolean ignoreResourceNotFound,
            int initialDelayMillis,
            int delayMillis,
            boolean ignoreDeletesFromSource) throws IOException {

        ifExistingPropertiesSourceThenThrowIllegalStateException();

        Map<String, String> parameterMap = getParameterMap(delayMillis, initialDelayMillis, ignoreDeletesFromSource, ignoreResourceNotFound);
        ConcurrentCompositeConfiguration config = addFileAndClasspathPropertyLocationsToConfiguration(parameterMap, locations);

        DynamicPropertyFactory.initWithConfigurationSource(config);
    }
    
    // TODO: Tidy this up
    protected ConcurrentCompositeConfiguration setMixedResourcesAsPropertySources(
            Map<String, String> parameterMap, 
            Resource[] locations, 
            Map<String, String> jdbcConnectionDetailMap) throws IOException {
        
        ifExistingPropertiesSourceThenThrowIllegalStateException();
        
        // TODO: add documentation for the effect of loading jdbc first and location as it divert from normal way of property overloading of Archaius.
        ConcurrentCompositeConfiguration conComConfiguration = new ConcurrentCompositeConfiguration();
        DynamicConfiguration dynamicConfiguration = buildDynamicConfigFromConnectionDetailsMap(jdbcConnectionDetailMap, parameterMap);
        conComConfiguration.addConfiguration(dynamicConfiguration);
        conComConfiguration = addFileAndClasspathPropertyLocationsToConfiguration(conComConfiguration, parameterMap, locations);

        DynamicPropertyFactory.initWithConfigurationSource(conComConfiguration);

        return conComConfiguration;
    }
    
    private ConcurrentCompositeConfiguration addFileAndClasspathPropertyLocationsToConfiguration(
            Map<String, String> parameterMap, 
            Resource[] locations) throws IOException {
        
        return addFileAndClasspathPropertyLocationsToConfiguration(new ConcurrentCompositeConfiguration(), parameterMap, locations);
    }
    
    
    private ConcurrentCompositeConfiguration addFileAndClasspathPropertyLocationsToConfiguration(
            ConcurrentCompositeConfiguration conComConfiguration,
            Map<String, String> parameterMap, 
            Resource[] locations) throws IOException {
        
        // TODO: This is duplication
        int initialDelayMillis = Integer.valueOf(parameterMap.get(JdbcContants.INITIAL_DELAY_MILLIS));
        int delayMillis = Integer.valueOf(parameterMap.get(JdbcContants.DELAY_MILLIS));
        boolean ignoreDeletesFromSource = Boolean.parseBoolean(parameterMap.get(JdbcContants.IGNORE_DELETE_FROMSOURCE));
        boolean ignoreResourceNotFound = Boolean.parseBoolean(parameterMap.get(JdbcContants.IGNORE_RESOURCE_NOTFOUND));
        
        // TODO: This is duplication
        for (int i = locations.length - 1; i >= 0; i--) {
            try {
                conComConfiguration.addConfiguration(new DynamicURLConfiguration(initialDelayMillis, delayMillis, ignoreDeletesFromSource, locations[i].getURL().toString()));
            } catch (Exception ex) {
                if (!ignoreResourceNotFound) {
                    LOGGER.error("Exception thrown when adding a configuration location.", ex);
                    throw ex;
                }
            }
        }
        
        return conComConfiguration;
    }

    protected Map<String, String> getParameterMap(int delayMillis, int initialDelayMillis, boolean ignoreDeleteFromSource, boolean ignoreResourceNotFound) {

        Map parameterMap = new HashMap();

        parameterMap.put(JdbcContants.DELAY_MILLIS, String.valueOf(delayMillis));
        parameterMap.put(JdbcContants.INITIAL_DELAY_MILLIS, String.valueOf(initialDelayMillis));
        parameterMap.put(JdbcContants.IGNORE_DELETE_FROMSOURCE, String.valueOf(ignoreDeleteFromSource));
        parameterMap.put(JdbcContants.IGNORE_RESOURCE_NOTFOUND, String.valueOf(ignoreResourceNotFound));

        return parameterMap;
    }
    
    public Map<String, String> extractJdbcParameters(String jdbcLocation) {
        if (jdbcLocation != null) {
            return createDatabaseKeyValueMap(jdbcLocation);
        } else {
            return null;
        }
    }
    
    private Map<String, String> createDatabaseKeyValueMap(String jdbcUri) {
        Map<String, String> jdbcMap = new HashMap<>();

        String delims = "[|][|]";

        // TODO: This is duplication of below
        if (jdbcUri == null) {
            LOGGER.info("Argument passed can't be null.");
            LOGGER.error("The arguments passed are not correct");
            LOGGER.error("Argument format is : driverClassName=<com.mysql.jdbc.Driver>||"
                    + "dbURL#<jdbc:mysql://localhost:3306/java>||username#<root>||password=<password>||"
                    + "sqlQuery#s<elect distinct property_key, property_value from MySiteProperties>||"
                    + "keyColumnName#<property_key>||valueColumnName#<property_value>");
        }

        String[] tokens = jdbcUri.split(delims);

        if (tokens.length != JdbcContants.EXPECTED_JDBC_PARAM_COUNT) {
            LOGGER.info("Argument passed: " + jdbcUri);
            LOGGER.error("The arguments passed are not correct");
            LOGGER.error("Argument format is : driverClassName=<com.mysql.jdbc.Driver>||"
                    + "dbURL#<jdbc:mysql://localhost:3306/java>||username#<root>||password=<password>||"
                    + "sqlQuery#s<elect distinct property_key, property_value from MySiteProperties>||"
                    + "keyColumnName#<property_key>||valueColumnName#<property_value>");
        } else {
            delims = "[#]";
            for (String keyValue : tokens) {
                String[] keyAndValue = keyValue.split(delims);
                jdbcMap.put(keyAndValue[0], keyAndValue[1]);
            }
        }
        return jdbcMap;
    }
    
    private void ifExistingPropertiesSourceThenThrowIllegalStateException() {
        if (DynamicPropertyFactory.getBackingConfigurationSource() != null) {
            LOGGER.error("There was already a config source (or sources) configured.");
            throw new IllegalStateException("Archaius is already configured with a property source/sources.");
        }
    }
    
    private DynamicConfiguration buildDynamicConfigFromConnectionDetailsMap(Map<String, String> jdbcConnectionDetailMap, Map<String, String> parameterMap) {
        
        int initialDelayMillis = Integer.valueOf(parameterMap.get(JdbcContants.INITIAL_DELAY_MILLIS));
        int delayMillis = Integer.valueOf(parameterMap.get(JdbcContants.DELAY_MILLIS));
        boolean ignoreDeletesFromSource = Boolean.parseBoolean(parameterMap.get(JdbcContants.IGNORE_DELETE_FROMSOURCE));
        
        DriverManagerDataSource ds = buildDataSourceFromConnectionDetailsMap(jdbcConnectionDetailMap);
        JDBCConfigurationSource source = buildJdbcConfigSourceFromConnectionDetailsMap(ds, jdbcConnectionDetailMap);
        FixedDelayPollingScheduler scheduler = new FixedDelayPollingScheduler(initialDelayMillis, delayMillis, ignoreDeletesFromSource);
        return new DynamicConfiguration(source, scheduler);
    }
    
    private JDBCConfigurationSource buildJdbcConfigSourceFromConnectionDetailsMap(DriverManagerDataSource ds, Map<String, String> jdbcConnectionDetailMap) {
        JDBCConfigurationSource source = new JDBCConfigurationSource(ds,
                jdbcConnectionDetailMap.get(JdbcContants.SQL_QUERY),
                jdbcConnectionDetailMap.get(JdbcContants.KEY_COLUMN_NAME),
                jdbcConnectionDetailMap.get(JdbcContants.VALUE_COLUMN_NAME));
        return source;
    }
    
    private DriverManagerDataSource buildDataSourceFromConnectionDetailsMap(Map<String, String> jdbcConnectionDetailMap) {
        DriverManagerDataSource ds = new DriverManagerDataSource(jdbcConnectionDetailMap.get(JdbcContants.DB_URL),
                jdbcConnectionDetailMap.get(JdbcContants.USERNAME),
                jdbcConnectionDetailMap.get(JdbcContants.PASSWORD));
        return ds;
    }
}
