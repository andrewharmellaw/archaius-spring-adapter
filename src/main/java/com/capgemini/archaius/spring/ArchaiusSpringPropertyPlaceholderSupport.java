package com.capgemini.archaius.spring;

import java.io.IOException;
import java.util.Properties;

import com.netflix.config.ConcurrentCompositeConfiguration;
import com.netflix.config.DynamicPropertyFactory;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

/**
 *
 * @author Andrew Harmel-Law
 */
class ArchaiusSpringPropertyPlaceholderSupport {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArchaiusSpringPropertyPlaceholderSupport.class);
    
    protected String resolvePlaceholder(String placeholder, Properties props, int systemPropertiesMode) {
        return DynamicPropertyFactory.getInstance().getStringProperty(placeholder, null).get();
    }
    
    protected void setLocation(Resource location) throws Exception {
        
        if (DynamicPropertyFactory.getBackingConfigurationSource() != null) {
            LOGGER.error("There was already a config source (or sources) configured.");
            throw new RuntimeException("Archaius is already configured with a property source/sources.");
        }
        
        ConcurrentCompositeConfiguration config = new ConcurrentCompositeConfiguration();
        config.addConfiguration(new PropertiesConfiguration(location.getURL()));

        DynamicPropertyFactory.initWithConfigurationSource(config);
    }
    
    protected void setLocations(Resource[] locations,  boolean ignoreResourceNotFound) throws Exception {
        
        if (DynamicPropertyFactory.getBackingConfigurationSource() != null) {
            LOGGER.error("There was already a config source (or sources) configured.");
            throw new Exception("Archaius is already configured with a property source/sources.");
        }
        
        ConcurrentCompositeConfiguration config = new ConcurrentCompositeConfiguration();
        for (int i = locations.length -1 ; i >= 0 ; i--) {
            try {
                config.addConfiguration(new PropertiesConfiguration(locations[i].getURL()));
            } catch (IOException | ConfigurationException ex) {
                if (ignoreResourceNotFound != true) {
                    LOGGER.error("IOException thrown when adding a configuration location.", ex);
                    throw ex;
                }
            }
        }

        DynamicPropertyFactory.initWithConfigurationSource(config);
    }
}
