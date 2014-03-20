package com.capgemini.archaius.spring;

import org.apache.camel.CamelContext;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.junit.Ignore;
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
import static org.hamcrest.CoreMatchers.not;

/**
 *
 * @author aharmel-law
 */
@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:camel/camelPropertiesLoadingTest.xml"})
@ActiveProfiles("default")
public class CamelPropertiesLoadingTest {
    
    private final String propertyKey = "{{var2}}";
    private String propertyValue;
    
    @Autowired
    @Qualifier("camel")
    protected CamelContext context;
    
    @DirtiesContext
    @Test
    public void camelPropertiesAreLoadedFromSingleFileAndAccessedViaTheCamelValueAnnotation() throws Exception {
        
        propertyValue = context.resolvePropertyPlaceholders(propertyKey);
        
        assertThat(context, is(not(null)));
        assertThat(propertyValue, is(equalTo("MY SECOND VAR")));
    }
}
