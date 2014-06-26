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

import java.util.HashMap;
import java.util.Properties;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.io.Resource;

import com.capgemini.archaius.spring.util.JdbcContants;

/**
 * 
 * @author Andrew Harmel-Law
 */
public class ArchaiusPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArchaiusPropertyPlaceholderConfigurer.class);
    
    public static final int DEFAULT_DELAY = 1000;
    
    private transient int initialDelayMillis = DEFAULT_DELAY;
    private transient int delayMillis = DEFAULT_DELAY;
    private transient boolean ignoreResourceNotFound = true;
    private transient boolean ignoreDeletesFromSource = true;

    private final transient ArchaiusSpringPropertyPlaceholderSupport propertyPlaceholderSupport 
            = new ArchaiusSpringPropertyPlaceholderSupport();
    
    private transient Map<String, String> jdbcConnectionDetailMap = null;
    
    /**
     * The initial delay before the property values are re-read from the location, in milliseconds
     *
     * @param initialDelayMillis
     */
    public void setInitialDelayMillis(int initialDelayMillis) {
        this.initialDelayMillis = initialDelayMillis;
    }

    /**
     * Set the delay for the property values to re-read from the location, in milliseconds
     *
     * @param delayMillis
     */
    public void setDelayMillis(int delayMillis) {
        this.delayMillis = delayMillis;
    }

    /**
     * Should the dynamic property loader ignore deletes from the location source.
     *
     * @param ignoreDeletesFromSource
     */
    public void setIgnoreDeletesFromSource(boolean ignoreDeletesFromSource) {
        this.ignoreDeletesFromSource = ignoreDeletesFromSource;
    }

    @Override
    public void setIgnoreResourceNotFound(boolean setting) {
        ignoreResourceNotFound = setting;
        super.setIgnoreResourceNotFound(setting);
    }
    
    @Override
    protected String resolvePlaceholder(String placeholder, Properties props, int systemPropertiesMode) {
        return propertyPlaceholderSupport.resolvePlaceholder(placeholder, props, systemPropertiesMode);
    }
    
    @Override
    public void setLocation(Resource location) {
        try {
            // TODO: Make this not get executed
            // If there is not also a JDBC locaiton
            if (jdbcConnectionDetailMap == null) {
                propertyPlaceholderSupport.setLocation(
                        location, initialDelayMillis, delayMillis, ignoreDeletesFromSource);
            } else {
                propertyPlaceholderSupport.setMixResourcesAsPropertySource(location, jdbcConnectionDetailMap);
            }
        } catch (Exception ex) {
            LOGGER.error("Problem setting the location.", ex);
            throw new RuntimeException("Problem setting the location.", ex);
        }
    }
    
    @Override
    public void setLocations(Resource[] locations) {
        try {
            propertyPlaceholderSupport.setLocations(
                    locations, ignoreResourceNotFound, initialDelayMillis, delayMillis, ignoreDeletesFromSource);
            super.setLocations(locations);
        } catch (Exception ex) {
            LOGGER.error("Problem setting the locations", ex);
            throw new RuntimeException("Problem setting the locations.", ex);
        }
    }
    
    // TODO: REMOVE THIS
    public void setjdbcLocation(String jdbcLocation) {
        if (jdbcLocation != null) {
            jdbcConnectionDetailMap = createDatabaseKeyValueMap(jdbcLocation);
        }
    }
    
    // TODO: Move this
    private Map<String, String> createDatabaseKeyValueMap(String jdbcUri) {
        Map<String, String> jdbcMap = new HashMap<>();

        String delims = "[|][|]";

        if (jdbcUri == null) {
            LOGGER.info("Argument passed Cant be null. ");
            LOGGER.error("The argument passes is not correct");
            LOGGER.error("Argument format to be passes is : driverClassName=<com.mysql.jdbc.Driver>||"
                    + "dbURL#<jdbc:mysql://localhost:3306/java>||username#<root>||password=<password>||"
                    + "sqlQuerry#s<elect distinct property_key, property_value from MySiteProperties>||"
                    + "keyColumnName#<property_key>||valueColumnName#<property_value>");
        }

        String[] tokens = jdbcUri.split(delims);

        if (tokens.length != JdbcContants.EXPECTED_JDBC_PARAM_COUNT) {
            LOGGER.info("Argument passed : " + jdbcUri);
            LOGGER.error("The argument passes is not correct");
            LOGGER.error("Argument format to be passes is : driverClassName=<com.mysql.jdbc.Driver>||"
                    + "dbURL#<jdbc:mysql://localhost:3306/java>||username#<root>||password=<password>||"
                    + "sqlQuerry#s<elect distinct property_key, property_value from MySiteProperties>||"
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
}
