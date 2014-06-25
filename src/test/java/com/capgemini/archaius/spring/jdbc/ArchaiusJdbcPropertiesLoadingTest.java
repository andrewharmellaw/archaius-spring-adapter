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

import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import com.capgemini.archaius.spring.jdbc.dataload.UpdateTestDataForArchaiusTest;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.DynamicStringProperty;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 *
 * @author Sanjay Kumar.
 */
@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/jdbc/archaiusJdbcPropertiesLoadingTest.xml"})
@ActiveProfiles("default")
public class ArchaiusJdbcPropertiesLoadingTest extends ArchaiusJdbcTest {

    private final String propertyArchaiusKey = "Error404";
    private final String originalExpectedArchaiusPropertyValue = "Page not found";
    private final String newExpectedArchaiusPropertyValue = "New Page not found";

    public Logger LOGGER = LoggerFactory.getLogger(ArchaiusJdbcPropertiesLoadingTest.class);

    @Test
    public void propertiesAreLoadedFromDbAndAccessedViaArchaiusDynamicStringProperty() throws InterruptedException {
        
        // When
        DynamicStringProperty propertyFromArchaiusJdbc = DynamicPropertyFactory.getInstance().getStringProperty(propertyArchaiusKey, propertyArchaiusKey);

        // Then
        assertThat(propertyFromArchaiusJdbc.get(), is(equalTo(originalExpectedArchaiusPropertyValue)));   
    }

    public void propertiesLoadedFromTheDbWhichAreChangedInTheDbAlsoChangeInJava() throws Exception {

        // When
        UpdateTestDataForArchaiusTest updateTestData = new UpdateTestDataForArchaiusTest();
        updateTestData.initializedDerby();
        Thread.sleep(100);

        // Then
        DynamicStringProperty propertyFromArchaiusJdbc 
                = DynamicPropertyFactory.getInstance().getStringProperty(propertyArchaiusKey, propertyArchaiusKey);
        assertThat(propertyFromArchaiusJdbc.get(), is(equalTo(newExpectedArchaiusPropertyValue)));
    }
}
