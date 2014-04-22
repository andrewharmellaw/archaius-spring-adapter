package com.capgemini.archaius.spring;

import java.util.Properties;

import com.netflix.config.ConcurrentCompositeConfiguration;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.DynamicURLConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

/**
 * This class builds the property configuration factory for the location(s) provided.
 * @author Andrew Harmel-Law
 * @author Nick Walter
 */
class ArchaiusSpringPropertyPlaceholderSupport {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArchaiusSpringPropertyPlaceholderSupport.class);
    
    protected String resolvePlaceholder(String placeholder, Properties props, int systemPropertiesMode) {
        return DynamicPropertyFactory.getInstance().getStringProperty(placeholder, null).get();
    }
    
    protected void setLocation(Resource location,
                               int initialDelayMillis,
                               int delayMillis,
                               boolean ignoreDeletesFromSource) throws Exception {
        
        if (DynamicPropertyFactory.getBackingConfigurationSource() != null) {
            LOGGER.error("There was already a config source (or sources) configured.");
            throw new RuntimeException("Archaius is already configured with a property source/sources.");
        }

        final String locationURL = location.getURL().toString();
        final DynamicURLConfiguration urlConfiguration = new DynamicURLConfiguration(
            initialDelayMillis, delayMillis, ignoreDeletesFromSource, locationURL
        );

        DynamicPropertyFactory.initWithConfigurationSource(urlConfiguration);
    }
    
    protected void setLocations(Resource[] locations,
                                boolean ignoreResourceNotFound,
                                int initialDelayMillis,
                                int delayMillis,
                                boolean ignoreDeletesFromSource) throws Exception {
        
        if (DynamicPropertyFactory.getBackingConfigurationSource() != null) {
            LOGGER.error("There was already a config source (or sources) configured.");
            throw new Exception("Archaius is already configured with a property source/sources.");
        }
        
        ConcurrentCompositeConfiguration config = new ConcurrentCompositeConfiguration();
        for (int i = locations.length -1 ; i >= 0 ; i--) {
            try {
                final String locationURL = locations[i].getURL().toString();
                config.addConfiguration(new DynamicURLConfiguration(
                        initialDelayMillis, delayMillis, ignoreDeletesFromSource, locationURL
                ));
            } catch (Exception ex) {
                if (ignoreResourceNotFound != true) {
                    LOGGER.error("Exception thrown when adding a configuration location.", ex);
                    throw ex;
                }
            }
        }

        DynamicPropertyFactory.initWithConfigurationSource(config);
    }
}
