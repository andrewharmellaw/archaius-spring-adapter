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
package com.capgemini.archaius.spring.jdbc;

import com.capgemini.archaius.spring.jdbc.derby.AbstractArchaiusJdbcTest;
import org.apache.camel.CamelContext;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.DynamicStringProperty;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

/**
 *
 * @author skumar81
 */
@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:camel/jdbc/camelPropertiesLoadingFromJdbcAndPropertiesFileTest.xml"})
@ActiveProfiles("default")
public class CamelPropertiesLoadingFromJdbcAndPropertiesFileTest extends AbstractArchaiusJdbcTest {

    private final String propertyArchaiusKey = "var2";
    private final String nonExistentPropertyKey = "bad_key";
    private final String expectedArchaiusPropertyValue = "MY SECOND VAR";

    @Autowired
    @Qualifier("camel")
    protected CamelContext context;

    @DirtiesContext
    @Test
    public void camelPropertiesAreLoadedFromSingleFileAndAccessedViaTheCamelValueAnnotation() throws Exception {

        // Given
        String camelPropertyValue = context.resolvePropertyPlaceholders("{{" + propertyArchaiusKey + "}}");

        // Then
        assertThat("The context cannot be null.", context != null);
        assertThat(camelPropertyValue, is(equalTo(expectedArchaiusPropertyValue)));
    }

    @Test
    public void nonExistentPropertiesWhenRequestedViaCamelThrowIllegalArgumentExceptions() throws InterruptedException {
        
        // When
        try {
            context.resolvePropertyPlaceholders("{{" + nonExistentPropertyKey + "}}");
            
            // Then
            fail("An IllegalArgument Exception shound be thrown");
        } catch (IllegalArgumentException ex) {
            assertThat(ex.getMessage(), is(equalTo("Property with key [" + nonExistentPropertyKey + "] not found in properties from text: {{" + nonExistentPropertyKey + "}}")));
        } catch (Exception ex) {
            fail("All other exceptions should not be thrown: " + ex.getMessage());
        }
    }
}
