package com.capgemini.archaius.spring;

import java.util.Properties;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

/**
 *
 * @author aharmel-law
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/springPropertiesLoadingTest.xml"})
@ActiveProfiles("default")
public class SpringPropertiesLoadingTest {

    private final String propertyKey = "var2";
    private final String expectedPropertyValue = "MY SECOND VAR";
    @Value("${" + propertyKey + "}") private String propertyValue;
    
    @Autowired 
    private ApplicationContext context;
    
    private Environment env;
    
    @After
    public void teardown() {
        // Clean up archaius properties here
    }
    
    @Test
    public void springPropertiesAreLoadedFromSingleFileAndAccessedViaTheSpringValueAnnotation() {
        assertThat(propertyValue, equalTo(expectedPropertyValue));
    }
    
    /**
     * Of course this works as we're just testing Spring - instead we need to document that you can;t do this any more
     * as it will ignore Archaius changes.
     * 
     * @throws Exception
     */
    @Test
    public void springPropertiesAreLoadedFromSingleFileAndAccessedViaPropertiesLoadersUtils() throws Exception {
        
        Resource resource = new ClassPathResource("properties/system.properties");
        Properties props = PropertiesLoaderUtils.loadProperties(resource);
        
        assertThat(props.containsKey(propertyKey), is(true));
    }
    
    @Test
    public void springPropertiesAreLoadedFromSingleFileAndAccessedViaContext() {
        
        System.out.print("Is context null?: ");
        System.out.println(context == null);   
        
        env = context.getEnvironment();
        System.out.print("Is env null?: ");
        System.out.println(env == null);
        System.out.print("Active profiles: ");
        for (String profile : env.getActiveProfiles()) {
            System.out.print("\t" + profile);
        }
        
//        assertThat(context, is(not(null)));
//        assertThat(env, is(not(null)));
        assertThat(env.getProperty(propertyKey), is(expectedPropertyValue));
    }
}
