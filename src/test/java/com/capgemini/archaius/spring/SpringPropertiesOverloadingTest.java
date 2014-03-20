package com.capgemini.archaius.spring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 *
 * @author aharmel-law
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/springPropertiesOverloadingTest.xml"})
@ActiveProfiles("default")
public class SpringPropertiesOverloadingTest {
    
    @Value("${var2}") private String propertyValue;
    
    @Test
    public void springPropertiesAreLoadedFromSingleFileAndAccessedViaTheSpringValueAnnotation() {
        assertThat(propertyValue, is(equalTo("MY SECOND VAR (THIS ONE WINS)")));
    }
}
