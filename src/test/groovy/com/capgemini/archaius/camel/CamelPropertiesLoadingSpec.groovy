/*
 * Copyright (C) 2014 Capgemini (oss@capgemini.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.capgemini.archaius.camel

import org.apache.camel.CamelContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

/**
 * @author Gayathri Thiyagarajan
 */
@ContextConfiguration(locations = "classpath:camel/camelPropertiesLoadingTest.xml")
@ActiveProfiles("default")
class CamelPropertiesLoadingSpec extends Specification {

    private final String propertyKey = "var2"
    private final String expectedPropertyValue = "MY SECOND VAR"
    private final String nonExistentPropertyKey = "bad_key"

    @Value('${var2}') private final String springPropertyValue

    @Autowired
    @Qualifier("camel")
    protected CamelContext context;

    def "can load camel properties from a single file and access via camel value annotation"() {
        given :
            String camelPropertyValue = context.resolvePropertyPlaceholders("{{" + propertyKey + "}}")
        expect:
            context != null
            camelPropertyValue == expectedPropertyValue
    }

    def "can also load Spring properties from a single file and access via spring value annotation"() {
        expect:
            springPropertyValue == expectedPropertyValue
    }

    def "non existent properties when requested via camel throw IllegalArgumentException"() {
        when :
            context.resolvePropertyPlaceholders("{{" + nonExistentPropertyKey + "}}")
        then:
            IllegalArgumentException ie = thrown()
            ie.getMessage() == "Property with key [" + nonExistentPropertyKey + "] not found in properties from text: {{" + nonExistentPropertyKey + "}}"
    }

}