package com.capgemini.archaius.camel

import org.apache.camel.CamelContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

/**
 * @author: Gayathri Thiyagarajan
 * @version: 1.0
 */
@ContextConfiguration(locations = "classpath:camel/camelPropertiesOverloadingTest.xml")
@ActiveProfiles("default")
class CamelPropertiesOverloadingSpec extends Specification {

    private final String camelPropertyKey = "{{var2}}";
    @Autowired
    @Qualifier("camel")
    protected CamelContext context;
    private String camelPropertyValue;
    @Value('${var2}') private String springPropertyValue;

    def "camel properties are loaded from multiple files and accessed via camel context" () {
        given :
            camelPropertyValue = context.resolvePropertyPlaceholders(camelPropertyKey);
        expect:
            context != null                               //The context cannot be null.
            camelPropertyValue == "MY SECOND VAR (THIS ONE WINS)"
    }

    def "spring properties are also loaded from multiple files and accessed via @Value annotation" () {
        expect:
            springPropertyValue == "MY SECOND VAR (THIS ONE WINS)"
    }
}
