package com.capgemini.archaius.spring

import org.springframework.beans.factory.annotation.Value
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

/**
 * @author: Gayathri Thiyagarajan
 * @version: 1.0
 */
@ActiveProfiles('default')
@ContextConfiguration(locations = 'classpath:spring/springPropertiesMissingFileIsOKTest.xml')
class SpringPropertiesMissingFileIsOkSpec extends Specification {

    @SuppressWarnings('GStringExpressionWithinString')
    @Value('${var2}') private final String propertyValue

    private final String expectedPropertyValue = 'MY SECOND VAR'

    def "missing spring properties files is ok if IgnoreResourceNotFound property set to true" () {
        expect:
        propertyValue == expectedPropertyValue
    }
}
