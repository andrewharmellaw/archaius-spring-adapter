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

import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.DynamicStringProperty;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.capgemini.archaius.spring.jdbc.derby.AbstractArchaiusJdbcTest;
import com.capgemini.archaius.spring.jdbc.derby.ArchaiusPropertyDataUpdater;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 *
 * @author Sanjay Kumar
 * @autrhor Andrew Harmel-Law
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/jdbc/archaiusJdbcPropertiesLoadingTest.xml"})
@ActiveProfiles("default")
public class ArchaiusJdbcPropertiesLoadingTest extends AbstractArchaiusJdbcTest {

    private final String propertyArchaiusKey = "Error404";
    private final String originalExpectedArchaiusPropertyValue = "Page not found";
    private final String newExpectedArchaiusPropertyValue = "New Page not found";

    public Logger LOGGER = LoggerFactory.getLogger(ArchaiusJdbcPropertiesLoadingTest.class);

    @Test
    public void propertiesLoadedFromTheDbWhichAreChangedInTheDbAlsoChangeInJava() throws Exception {

        // Given
        DynamicStringProperty propertyFromArchaiusJdbc 
                = DynamicPropertyFactory.getInstance().getStringProperty(propertyArchaiusKey, "The property for key: " + propertyArchaiusKey + " was not found");
        assertThat(propertyFromArchaiusJdbc.get(), is(equalTo(originalExpectedArchaiusPropertyValue)));   
        
        // When
        ArchaiusPropertyDataUpdater.updatePropertyData();
        Thread.sleep(1000); // give the Archaius poller time to read the updated value

        // Then
        propertyFromArchaiusJdbc = DynamicPropertyFactory.getInstance().getStringProperty(propertyArchaiusKey, "The property for key: " + propertyArchaiusKey + " was not found");
        assertThat(propertyFromArchaiusJdbc.get(), is(equalTo(newExpectedArchaiusPropertyValue)));
    }
}
