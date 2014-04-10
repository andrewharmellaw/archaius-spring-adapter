package com.capgemini.archaius.spring;

import org.junit.Test;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 *
 * @author aharmel-law
 */
@ActiveProfiles("default")
public class CamelPropertiesMissingFileIsNotOKTest {
    
    @Test
    public void missingSpringPropertiesFilesIsNotOkIfIgnoreResourceNotFoundPropertySetToFalse() {
        // Load the context
        try {
            new ClassPathXmlApplicationContext("camel/camelPropertiesMissingFileIsNotOKTest.xml");
            fail("An exception should have been thrown when loading the context because the class path resource [META-INF/file-not-there.properties] cannot be resolved to a URL because it does not exist");
        } catch (BeanCreationException ex) { 
            assertTrue(ex.getMessage().startsWith("Error creating bean with name 'com.capgemini.archaius.spring.ArchaiusBridgePropertyPlaceholderConfigurer#0' defined in class path resource [camel/camelPropertiesMissingFileIsNotOKTest.xml]: Error setting property values"));
        } 
    }
}
