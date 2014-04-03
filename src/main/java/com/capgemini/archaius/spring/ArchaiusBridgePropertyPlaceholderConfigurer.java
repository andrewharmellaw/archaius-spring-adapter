package com.capgemini.archaius.spring;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.camel.spring.spi.BridgePropertyPlaceholderConfigurer;
import org.springframework.core.io.Resource;

/**
 * 
 * @author Andrew Harmel-Law
 */
public class ArchaiusBridgePropertyPlaceholderConfigurer extends BridgePropertyPlaceholderConfigurer {
    
    private ArchaiusSpringPropertyPlaceholderSupport propertyPlaceholderSupport = new ArchaiusSpringPropertyPlaceholderSupport();
    
    @Override
    protected String resolvePlaceholder(String placeholder, Properties props, int systemPropertiesMode) {
        return propertyPlaceholderSupport.resolvePlaceholder(placeholder, props, systemPropertiesMode);
    }
    
    @Override
    public void setLocation(Resource location) {
        propertyPlaceholderSupport.setLocation(location);
        super.setLocation(location);
    }
    
    @Override
    public void setLocations(Resource[] locations) {
        try {
            propertyPlaceholderSupport.setLocations(locations);
        } catch (Exception ex) {
            Logger.getLogger(ArchaiusBridgePropertyPlaceholderConfigurer.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException("Problem setting the locations.", ex);
        }
        super.setLocations(locations);
    }
}
