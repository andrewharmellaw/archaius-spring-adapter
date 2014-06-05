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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.io.Resource;

import java.util.Properties;

/**
 * 
 * 
 * @author Andrew Harmel-Law
 */
public class ArchaiusPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ArchaiusPropertyPlaceholderConfigurer.class);
    
    private final ArchaiusSpringPropertyPlaceholderSupport propertyPlaceholderSupport = new ArchaiusSpringPropertyPlaceholderSupport();
    private boolean ignoreResourceNotFound = true;

    // settings for dynamic property configuration
    private int initialDelayMillis = 1000;
    private int delayMillis = 1000;
    private boolean ignoreDeletesFromSource = true;

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
            propertyPlaceholderSupport.setLocation(
                    location, initialDelayMillis, delayMillis, ignoreDeletesFromSource);
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
}