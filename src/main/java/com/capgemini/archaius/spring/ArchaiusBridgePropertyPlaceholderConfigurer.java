package com.capgemini.archaius.spring;

import java.util.Properties;
import org.apache.camel.CamelContext;
import org.apache.camel.component.properties.PropertiesResolver;

import org.apache.camel.spring.spi.BridgePropertyPlaceholderConfigurer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.io.Resource;

/**
 * 
 * 
 * @author Andrew Harmel-Law
 */
public class ArchaiusBridgePropertyPlaceholderConfigurer extends BridgePropertyPlaceholderConfigurer {
    
    private ArchauisSpringPropertyPlaceholderSupport propertyPlaceholderSupport = new ArchauisSpringPropertyPlaceholderSupport();
    private final Properties properties = new Properties(); // Lifted totally from the Camel class
    private PropertiesResolver resolver; // Lifted totally from the Camel class
    private String id; // Lifted totally from the Camel class

    // Lifted totally from the Camel class
    @Override
    public void setBeanName(String beanName) {
        this.id = beanName;
        super.setBeanName(beanName);
    }
    
    // Lifted totally from the Camel class
    @Override 
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {
 
        // store all the spring properties so we can refer to them later
        properties.putAll(props); 
        super.processProperties(beanFactoryToProcess, props);

    }

    
    // Lifted totally from the Camel class
    @Override
    public Properties resolveProperties(CamelContext context, boolean ignoreMissingLocation, String ... uris) throws Exception {
        
        Properties answer = new Properties();
 
        for (String uri : uris) {
            String ref = "ref:" + id;
 
            if (ref.equals(uri)) {
 
                answer.putAll(properties);

            } else if (resolver != null) {
 
                Properties p = resolver.resolveProperties(context, ignoreMissingLocation, uri);
 
                if (p != null) {
                    answer.putAll(p);
                }
            } 
        }

        // must not return null
        return answer;
    }
    
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
    
//    /**
//     * Set if failure to find the property resource should be ignored.
//     * <p>"true" is appropriate if the properties file is completely optional.
//     * Default is "false".
//     */
//    public void setIgnoreResourceNotFound(boolean ignoreResourceNotFound) {
//        this.ignoreResourceNotFound = ignoreResourceNotFound;
//    }
}
