package com.capgemini.archaius.spring;

import org.junit.Test;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ActiveProfiles;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

/**
 *
 * @author aharmel-law
 */
@ActiveProfiles("default")
public class SpringPropertiesMissingFileIsNotOKTest {
    
    @Test
    public void missingSpringPropertiesFilesIsNotOkIfIgnoreResourceNotFoundPropertySetToFalse() {
        
        try {
            new ClassPathXmlApplicationContext("spring/springPropertiesMissingFileIsNotOKTest.xml");
            fail("An exception should have been thrown when loading the context because the class path resource [META-INF/file-not-there.properties] cannot be resolved to a URL because it does not exist");
        } catch (BeanCreationException ex) { 
            assertThat(ex.getCause().getMessage(), is(equalTo("Failed properties: Property 'locations' threw exception; nested exception is java.lang.RuntimeException: Problem setting the locations.")));
        }
    }
}
