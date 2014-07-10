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
import java.util.Map;
import java.util.Properties;

import com.netflix.config.ConcurrentCompositeConfiguration;
import org.apache.camel.spring.spi.BridgePropertyPlaceholderConfigurer;
import org.apache.commons.configuration.ConfigurationConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

/**
 *
 * @author Andrew Harmel-Law
 */
public class ArchaiusBridgePropertyPlaceholderConfigurer extends BridgePropertyPlaceholderConfigurer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArchaiusBridgePropertyPlaceholderConfigurer.class);
    
    public static final int DEFAULT_DELAY = 1000;
    
    private transient int initialDelayMillis = DEFAULT_DELAY;
    private transient int delayMillis = DEFAULT_DELAY;
    private transient boolean ignoreResourceNotFound;
    private transient boolean ignoreDeletesFromSource = true;
    
    private final transient ArchaiusSpringPropertyPlaceholderSupport propertyPlaceholderSupport
            = new ArchaiusSpringPropertyPlaceholderSupport();
    
    private transient Map<String, String> jdbcConnectionDetailMap = null;

    @Override
    public void setIgnoreResourceNotFound(boolean setting) {
        ignoreResourceNotFound = setting;
        super.setIgnoreResourceNotFound(setting);
    }

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
    protected String resolvePlaceholder(String placeholder, Properties props, int systemPropertiesMode) {
        return propertyPlaceholderSupport.resolvePlaceholder(placeholder, props, systemPropertiesMode);
    }

    /**
     * Archaius JDBC Connection URI.
     * 
     * @param jdbcLocation the URI from the jdbcLocation property in the Spring config
     */
    public void setjdbcLocation(String jdbcLocation) {
        jdbcConnectionDetailMap = propertyPlaceholderSupport.extractJdbcParamaters(jdbcLocation);
    }
    
    @Override
    public void setLocation(Resource location) {
        try {
            // If there is not also a JDBC locaiton
            if (jdbcConnectionDetailMap == null) {
                propertyPlaceholderSupport.setLocation(location, initialDelayMillis, delayMillis, ignoreDeletesFromSource);
                super.setLocation(location);
            } else {
                Map parameterMap = propertyPlaceholderSupport.getParameterMap(delayMillis, initialDelayMillis, ignoreDeletesFromSource, ignoreResourceNotFound);
                ConcurrentCompositeConfiguration conComConfiguration = propertyPlaceholderSupport.setMixedResourcesAsPropertySources(parameterMap, location, jdbcConnectionDetailMap);
                super.setProperties(ConfigurationConverter.getProperties(conComConfiguration));
            }
        } catch (IOException ex) {
            LOGGER.error("Problem setting the location.", ex);
            throw new RuntimeException("Problem setting the location.", ex);
        }
    }

    
    // This is almost exactly the same as it's counterpart
    @Override
    public void setLocations(Resource[] locations) {
        try {
            if (jdbcConnectionDetailMap == null) {
                propertyPlaceholderSupport.setLocations(locations, ignoreResourceNotFound, initialDelayMillis, delayMillis, ignoreDeletesFromSource);
                super.setLocations(locations);
            } else {
                Map parameterMap = propertyPlaceholderSupport.getParameterMap(delayMillis, initialDelayMillis, ignoreDeletesFromSource, ignoreResourceNotFound);
                ConcurrentCompositeConfiguration conComConfiguration = propertyPlaceholderSupport.setMixedResourcesAsPropertySources(parameterMap, locations, jdbcConnectionDetailMap);
                super.setProperties(ConfigurationConverter.getProperties(conComConfiguration));
            }
        } catch (IOException ex) {
            LOGGER.error("Problem setting the locations", ex);
            throw new RuntimeException("Problem setting the locations.", ex);
        }
    }
}
