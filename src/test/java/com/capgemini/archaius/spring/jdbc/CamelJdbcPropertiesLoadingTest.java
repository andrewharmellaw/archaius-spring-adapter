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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import com.capgemini.archaius.spring.jdbc.derby.ArchaiusPropertyDataUpdater;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.not;

/**
 *
 * @author Sanjay Kumar
 */
@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:camel/jdbc/camelJdbcPropertiesLoadingTest.xml"})
@ActiveProfiles("default")
public class CamelJdbcPropertiesLoadingTest extends AbstractArchaiusJdbcTest {

    @Autowired
    @Qualifier("camel")
    protected CamelContext context;

    private final String propertyArchaiusKey = "Error404";
    private final String originalExpectedArchaiusPropertyValue = "Page not found";
    private final String newExpectedArchaiusPropertyValue = "New Page not found";

    @DirtiesContext
    @Test
    public void camelPropertiesLoadedFromTheDbWhichAreChangedInDbAtRuntimeAreNotReflectedInCamelTest() throws Exception {
        
        // When
        String camelPropertyValueTwo = context.resolvePropertyPlaceholders("{{" + propertyArchaiusKey + "}}");

        // Then
        assertThat("The context cannot be null.", context != null);
        assertThat(camelPropertyValueTwo, is(equalTo(originalExpectedArchaiusPropertyValue)));
        
        // When property is then changed in the DB
        ArchaiusPropertyDataUpdater updateTestData = new ArchaiusPropertyDataUpdater();
        updateTestData.updatePropertyData();
        Thread.sleep(100);

        // Then
        String camelPropertyValue = context.resolvePropertyPlaceholders("{{" + propertyArchaiusKey + "}}");

        assertThat("The context cannot be null.", context != null);
        assertThat(camelPropertyValue, is(equalTo(originalExpectedArchaiusPropertyValue)));
        assertThat(camelPropertyValue, is(not(newExpectedArchaiusPropertyValue)));
    }
}
