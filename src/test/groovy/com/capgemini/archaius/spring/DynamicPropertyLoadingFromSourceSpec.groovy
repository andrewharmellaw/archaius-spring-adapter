package com.capgemini.archaius.spring

import com.netflix.config.DynamicPropertyFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import org.springframework.core.io.support.PropertiesLoaderUtils
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

/**
 * @author: Gayathri Thiyagarajan
 * @version: 1.0
 */

@ContextConfiguration(locations = "classpath:spring/dynamicPropertyLoadingFromSourceTest.xml")
@ActiveProfiles("default")
class DynamicPropertyLoadingFromSourceSpec extends Specification {

    public static final String UPDATED_PROPERTY_VALUE = "MY SECOND VAR UPDATED!!";
    public static final String PROPERTY_VALUE = "MY SECOND VAR";
    private String propertyValue

    def setup() throws IOException, InterruptedException {
        updatePropertiesFile(PROPERTY_VALUE);
        Thread.sleep(100);
    }

    def updatePropertiesFile(propValue) throws IOException {
        Properties prop = new Properties();
        prop.load(new ClassPathResource("/META-INF/system.properties").getInputStream());
        prop.setProperty("var2", propValue);
        FileOutputStream out = new FileOutputStream(new ClassPathResource("/META-INF/system.properties").getFile());
        prop.store(out, null);
        out.close();
    }

    def "update property value and check if it is reflected dynamically" () {
        when: "check initial property value"
            propertyValue = DynamicPropertyFactory.getInstance().getStringProperty("var2", null).getValue();
        then:
            propertyValue == PROPERTY_VALUE

        when: "change property value"
            updatePropertiesFile(UPDATED_PROPERTY_VALUE);
            Thread.sleep(100)
            propertyValue = DynamicPropertyFactory.getInstance().getStringProperty("var2", null).getValue();
        then:
            propertyValue == UPDATED_PROPERTY_VALUE

        when: "change property value back to original"
            updatePropertiesFile(PROPERTY_VALUE);
            Thread.sleep(100)
            propertyValue = DynamicPropertyFactory.getInstance().getStringProperty("var2", null).getValue();
        then:
            propertyValue == PROPERTY_VALUE
    }

    def "deleting property value in the file removes properties" () {
        when: "check initial property value"
            propertyValue = DynamicPropertyFactory.getInstance().getStringProperty("var2", null).getValue();
        then:
            propertyValue == PROPERTY_VALUE

        when: "remove property from file"
            removeProperty();
            Thread.sleep(100);
            propertyValue = DynamicPropertyFactory.getInstance().getStringProperty("var2", null).getValue();
        then:
            propertyValue == null;

        when: "change property value back to original"
            updatePropertiesFile(PROPERTY_VALUE);
            Thread.sleep(100)
            propertyValue = DynamicPropertyFactory.getInstance().getStringProperty("var2", null).getValue();
        then:
            propertyValue == PROPERTY_VALUE
    }

    def removeProperty() {
        Properties prop = new Properties();
        prop.load(new ClassPathResource("/META-INF/system.properties").getInputStream());
        prop.remove("var2");
        FileOutputStream out = new FileOutputStream(new ClassPathResource("/META-INF/system.properties").getFile());
        prop.store(out, null);
        out.close();
    }
}