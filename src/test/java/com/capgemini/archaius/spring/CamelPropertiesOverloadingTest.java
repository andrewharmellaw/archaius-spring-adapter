package com.capgemini.archaius.spring;

import org.apache.camel.CamelContext;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import org.springframework.beans.factory.annotation.Value;

/**
 *
 * @author aharmel-law
 */
@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:camel/camelPropertiesOverloadingTest.xml"})
@ActiveProfiles("default")
public class CamelPropertiesOverloadingTest {
    
    private final String camelPropertyKey = "{{var2}}";
    private String camelPropertyValue;
    @Value("${var2}") private String springPropertyValue;
    
    @Autowired
    @Qualifier("camel")
    protected CamelContext context;
    
    @DirtiesContext
    @Test
    public void camelPropertiesAreLoadedFromMultipleFilesInOrderAndAccessedViaTheCamelContext() throws Exception {
        
        camelPropertyValue = context.resolvePropertyPlaceholders(camelPropertyKey);
        
        assertThat("The context cannot be null.", context != null);
        assertThat(camelPropertyValue, is(equalTo("MY SECOND VAR (THIS ONE WINS)")));
    }
    
    @Test
    public void springPropertiesAreAlsoLoadedFromMultipleFilesInOrderAndAccessedViaTheSpringValueAnnotation() {
        assertThat(springPropertyValue, is(equalTo("MY SECOND VAR (THIS ONE WINS)")));
    }
}
