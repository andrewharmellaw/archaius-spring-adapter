package com.capgemini.archaius.spring;

import java.util.Properties;

import org.apache.camel.spring.spi.BridgePropertyPlaceholderConfigurer;
import org.springframework.core.io.Resource;

/**
 * 
 * @author Andrew Harmel-Law
 */
public class ArchaiusBridgePropertyPlaceholderConfigurer extends BridgePropertyPlaceholderConfigurer {
    
    private ArchauisSpringPropertyPlaceholderSupport propertyPlaceholderSupport = new ArchauisSpringPropertyPlaceholderSupport();
    
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
        propertyPlaceholderSupport.setLocations(locations);
        super.setLocations(locations);
    }
}
