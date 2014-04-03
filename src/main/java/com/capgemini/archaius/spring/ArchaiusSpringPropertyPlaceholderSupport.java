package com.capgemini.archaius.spring;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.netflix.config.ConcurrentCompositeConfiguration;
import com.netflix.config.DynamicPropertyFactory;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.core.io.Resource;

/**
 *
 * @author Andrew Harmel-Law
 */
class ArchaiusSpringPropertyPlaceholderSupport {
    
    protected String resolvePlaceholder(String placeholder, Properties props, int systemPropertiesMode) {
        return DynamicPropertyFactory.getInstance().getStringProperty(placeholder, "this is the default value - it's not defined in any properties source yet!").get();
    }
    
    protected void setLocation(Resource location) {
        
        if (DynamicPropertyFactory.getBackingConfigurationSource() != null) {
            Logger.getLogger(ArchaiusPropertyPlaceholderConfigurer.class.getName()).log(Level.SEVERE, "There was already a config source (or sources) configured");
            throw new RuntimeException("Archaius is already configured with a property source/sources.");
        }
        
        try {
            ConcurrentCompositeConfiguration config = new ConcurrentCompositeConfiguration();
            config.addConfiguration(new PropertiesConfiguration(location.getURL()));
        
            DynamicPropertyFactory.initWithConfigurationSource(config);
        } catch (IOException ex) {
            Logger.getLogger(ArchaiusPropertyPlaceholderConfigurer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ConfigurationException ex) {
            Logger.getLogger(ArchaiusPropertyPlaceholderConfigurer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    protected void setLocations(Resource[] locations) throws Exception {
        
        if (DynamicPropertyFactory.getBackingConfigurationSource() != null) {
            Logger.getLogger(ArchaiusPropertyPlaceholderConfigurer.class.getName()).log(Level.SEVERE, "There was already a config source (or sources) configured");
            throw new RuntimeException("Archaius is already configured with a property source/sources.");
        }
        
        try {
            ConcurrentCompositeConfiguration config = new ConcurrentCompositeConfiguration();
            for (int i = locations.length -1 ; i >= 0 ; i--) {
                try {
                    config.addConfiguration(new PropertiesConfiguration(locations[i].getURL()));
                } catch (IOException ex) {
                    Logger.getLogger(ArchaiusPropertyPlaceholderConfigurer.class.getName()).log(Level.SEVERE, null, ex);
//                    if (super.ignoreResourceNotFound != true) throw ex;
                } 
            }
            
            DynamicPropertyFactory.initWithConfigurationSource(config);
        }  catch (ConfigurationException ex) {
            Logger.getLogger(ArchaiusPropertyPlaceholderConfigurer.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
    }
}
