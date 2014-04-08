package com.capgemini.archaius.spring;

import org.junit.Test;
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
public class CamelPropertiesMissingFileIsNotOKTest {
    
    @Test
    public void missingSpringPropertiesFilesIsNotOkIfIgnoreResourceNotFoundPropertySetToFalse() {
        // Load the context
        try {
            ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("camel/camelPropertiesMissingFileIsNotOKTest.xml");
            fail("An exception should have been thrown when loading the context because the class path resource [META-INF/file-not-there.properties] cannot be resolved to a URL because it does not exist");
        } catch (Exception ex) { 
            assertThat(ex.getCause().getMessage(), is(equalTo("class path resource [META-INF/file-not-there.properties] cannot be opened because it does not exist")));
        }
    }
}
