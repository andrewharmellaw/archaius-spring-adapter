package com.capgemini.archaius.spring

import org.springframework.beans.factory.annotation.Value
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

/**
 * @author: Gayathri Thiyagarajan
 * @version: 1.0
 */

@ContextConfiguration(locations = 'classpath:spring/springPropertiesOverloadingTest.xml')
@ActiveProfiles('default')
class SpringPropertiesOverloadingSpec extends Specification {

    @SuppressWarnings('GStringExpressionWithinString')
    @Value('${var2}') private final String propertyValue
    private final String expectedPropertyValue = 'MY SECOND VAR (THIS ONE WINS)'

    def "spring property loaded from multiple files in order and accessed via annotation" () {
        expect:
        propertyValue == expectedPropertyValue
    }
}
