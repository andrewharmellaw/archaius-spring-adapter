package com.capgemini.archaius.spring;

import org.apache.camel.CamelContext;
import static org.hamcrest.CoreMatchers.equalTo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.DirtiesContext;

/**
 *
 * @author aharmel-law
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:camel/camelPropertiesMissingFileIsOKTest.xml"})
@ActiveProfiles("default")
public class CamelPropertiesMissingFileIsOKTest {

    private final String propertyKey = "{{var2}}";
    private String propertyValue;
    
    @Autowired
    @Qualifier("camel")
    protected CamelContext context;
    
    @DirtiesContext
    @Test
    public void camelPropertiesAreLoadedFromMultipleFilesInOrderAndAccessedViaTheCamelContext() throws Exception {
        
        propertyValue = context.resolvePropertyPlaceholders(propertyKey);
        
        assertThat("The context cannot be null.", context != null);
        assertThat(propertyValue, is(equalTo("MY SECOND VAR")));
    }
    
}
