package com.capgemini.archaius.spring

import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import org.springframework.core.io.support.PropertiesLoaderUtils
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

/**
 * @author Gayathri Thiyagarajan
 */
@ContextConfiguration(locations = "classpath:spring/springPropertiesLoadingTest.xml")
@ActiveProfiles("default")
class SpringPropertiesLoadingSpec extends Specification {

    // TODO Spring @Configuration tests
    // TODO Spring @Environment.getProperty tests

    private final String propertyKey = "var2"
    private final String expectedPropertyValue = "MY SECOND VAR"
    @Value('${var2}') private final String propertyValue

    def "can load Spring properties from a single file and access via an annotation"() {
        expect:
        propertyValue == expectedPropertyValue
    }

    /**
     * Of course this works as we're just testing Spring - instead we need to document that you can't do this any more
     * as it will ignore Archaius changes.
     */
    def "can load Spring properties from a single file and access via properties loader util"() {
        given:
        Resource resource = new ClassPathResource("properties/system.properties")

        when:
        Properties props = PropertiesLoaderUtils.loadProperties(resource)

        then:
        props.containsKey(propertyKey)
    }
}