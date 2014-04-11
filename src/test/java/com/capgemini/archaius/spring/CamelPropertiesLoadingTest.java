package com.capgemini.archaius.spring;

import org.apache.camel.CamelContext;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.fail;

/**
 *
 * @author aharmel-law
 */
@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:camel/camelPropertiesLoadingTest.xml"})
@ActiveProfiles("default")
public class CamelPropertiesLoadingTest {

    // TODO: test non-existent spring properites loaded via @Value
    
    private final String propertyKey = "var2";
    private final String nonExistentPropertyKey = "bad_key";
    @Value("${" + propertyKey + "}") private String springPropertyValue;
    private final String expectedPropertyValue = "MY SECOND VAR";
    
    @Autowired
    @Qualifier("camel")
    protected CamelContext context;
    
    @DirtiesContext
    @Test
    public void camelPropertiesAreLoadedFromSingleFileAndAccessedViaTheCamelValueAnnotation() throws Exception {
        
        String camelPropertyValue = context.resolvePropertyPlaceholders("{{" + propertyKey + "}}");
        
        assertThat("The context cannot be null.", context != null);
        assertThat(camelPropertyValue, is(equalTo(expectedPropertyValue)));
    }
    
    @Test
    public void springPropertiesAreAlsoLoadedOKFromSingleFileAndAccessedViaTheSpringValueAnnotation() {
        assertThat(springPropertyValue, equalTo(expectedPropertyValue));
    }
    
    @Test
    public void nonExistentPropertiesWhenrequestedViaCamelThrowIllegalArgumentExceptions() {
        
        try {
            context.resolvePropertyPlaceholders("{{" + nonExistentPropertyKey + "}}");
            fail("An IllegalArgument Exception shound be thrown");
        } catch (IllegalArgumentException ex) {
            assertThat(ex.getMessage(), is(equalTo("Property with key [" + nonExistentPropertyKey + "] not found in properties from text: {{" + nonExistentPropertyKey + "}}")));
        }catch (Exception ex) {
            fail("All other exceptions should not be thrown: " + ex.getMessage());
        }
    }
}
