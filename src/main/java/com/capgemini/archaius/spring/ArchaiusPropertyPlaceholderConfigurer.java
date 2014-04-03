package com.capgemini.archaius.spring;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.io.Resource;

/**
 * 
 * 
 * @author Andrew Harmel-Law
 */
public class ArchaiusPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
    
    private ArchaiusSpringPropertyPlaceholderSupport propertyPlaceholderSupport = new ArchaiusSpringPropertyPlaceholderSupport();
    
    private boolean ignoreResourceNotFound = true;
    
    @Override
    public void setIgnoreResourceNotFound(boolean setting) {
        ignoreResourceNotFound = setting;
        propertyPlaceholderSupport.setIgnoreResourceNotFound(ignoreResourceNotFound);
    }
    
    @Override
    protected String resolvePlaceholder(String placeholder, Properties props, int systemPropertiesMode) {
        return propertyPlaceholderSupport.resolvePlaceholder(placeholder, props, systemPropertiesMode);
    }
    
    @Override
    public void setLocation(Resource location) {
        propertyPlaceholderSupport.setLocation(location);
    }
    
    @Override
    public void setLocations(Resource[] locations) {
        try {
            propertyPlaceholderSupport.setLocations(locations);
        } catch (Exception ex) {
            Logger.getLogger(ArchaiusPropertyPlaceholderConfigurer.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException("Problem setting the locations.", ex);
        }
    }
}