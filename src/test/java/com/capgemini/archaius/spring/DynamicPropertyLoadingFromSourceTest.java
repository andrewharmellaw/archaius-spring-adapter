package com.capgemini.archaius.spring;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import com.netflix.config.DynamicPropertyFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test class for testing setting the dynamic property loading from source locations
 *
 * @author Nick Walter
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/dynamicPropertyLoadingFromSourceTest.xml"})
@ActiveProfiles("default")
public class DynamicPropertyLoadingFromSourceTest {

    public static final String UPDATED_PROPERTY_VALUE = "MY SECOND VAR UPDATED!!";
    public static final String PROPERTY_VALUE = "MY SECOND VAR";

    private String propertyValue;

    @Before
    public void setup() throws IOException, InterruptedException {
        updatePropertiesFile(PROPERTY_VALUE);
        Thread.sleep(100);

    }

    @Test
    public void testChangingTheValue() throws IOException, InterruptedException {

        // check the value now
        propertyValue = DynamicPropertyFactory.getInstance().getStringProperty("var2", null).getValue();
        assertEquals("MY SECOND VAR", propertyValue);

        // change the value in the file
        updatePropertiesFile(UPDATED_PROPERTY_VALUE);

        // check the value now
        Thread.sleep(100);
        propertyValue = DynamicPropertyFactory.getInstance().getStringProperty("var2", null).getValue();
        assertEquals(UPDATED_PROPERTY_VALUE, propertyValue);

        // change the value back
        updatePropertiesFile(PROPERTY_VALUE);

        // check the value now
        Thread.sleep(100);
        propertyValue = DynamicPropertyFactory.getInstance().getStringProperty("var2", null).getValue();
        assertEquals(PROPERTY_VALUE, propertyValue);
    }

    @Test
    public void testDeletingAValueFromSource() throws IOException, InterruptedException {
        // check the value now
        propertyValue = DynamicPropertyFactory.getInstance().getStringProperty("var2", null).getValue();
        assertEquals("MY SECOND VAR", propertyValue);

        // remove all values
        Properties prop = new Properties();
        prop.load(new ClassPathResource("/META-INF/system.properties").getInputStream());
        prop.remove("var2");
        FileOutputStream out = new FileOutputStream(new ClassPathResource("/META-INF/system.properties").getFile());
        prop.store(out, null);
        out.close();

        // check the value now
        Thread.sleep(100);
        propertyValue = DynamicPropertyFactory.getInstance().getStringProperty("var2", null).getValue();
        assertNull( propertyValue);

        // change the value back
        updatePropertiesFile(PROPERTY_VALUE);

        // check the value now
        Thread.sleep(100);
        propertyValue = DynamicPropertyFactory.getInstance().getStringProperty("var2", null).getValue();
        assertEquals(PROPERTY_VALUE, propertyValue);
    }

    /**
     * Update the property value and save the value to the file.
     * @param propertyValue the value to set
     * @throws IOException
     */
    private void updatePropertiesFile(String propertyValue) throws IOException {
        // change the value back
        Properties prop = new Properties();
        prop.load(new ClassPathResource("/META-INF/system.properties").getInputStream());
        prop.setProperty("var2", propertyValue);
        FileOutputStream out = new FileOutputStream(new ClassPathResource("/META-INF/system.properties").getFile());
        prop.store(out, null);
        out.close();
    }
}
