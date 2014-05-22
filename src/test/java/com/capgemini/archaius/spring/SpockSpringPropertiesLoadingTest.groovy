package com.capgemini.archaius.spring

import org.springframework.beans.factory.annotation.Value
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification


/**
 * This class
 * @author: Gayathri Thiyagarajan
 * @version: 1.0
 */

@ContextConfiguration(locations = "classpath:spring/springPropertiesLoadingTest.xml")
@ActiveProfiles("default")
class SpockSpringPropertiesLoadingTest extends Specification {

    private final String propertyKey = "var2";
    private final String expectedPropertyValue = "MY SECOND VAR";
    private String key = "#{" + propertyKey + "}";
//    @Value( "#{var2}") private String propertyValue;
    @Value('${"' + propertyKey + '"}') private String propertyValue;

    def "properties loaded from single file and access via annotation"() {
        expect:
        propertyValue == expectedPropertyValue;
    }
}