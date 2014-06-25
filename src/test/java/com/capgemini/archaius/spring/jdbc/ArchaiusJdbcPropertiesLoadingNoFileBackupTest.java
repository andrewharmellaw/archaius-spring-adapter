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

import org.junit.Test;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.DynamicStringProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ActiveProfiles;

import com.capgemini.archaius.spring.jdbc.derby.AbstractArchaiusJdbcTest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 *
 * @author Sanjay Kumar.
 */
@ActiveProfiles("default")
public class ArchaiusJdbcPropertiesLoadingNoFileBackupTest extends AbstractArchaiusJdbcTest {

    public Logger LOGGER = LoggerFactory.getLogger(ArchaiusJdbcPropertiesLoadingNoFileBackupTest.class);

    private final String propertyArchaiusKey = "Error404";
    private final String expectedArchaiusPropertyValue = "NO PROPERTY SET";

    @Test
    public void missingSpringPropertiesFilesIsNotOkIfJdbcLocationIsSpecified() {

        // Given
        new ClassPathXmlApplicationContext("spring/jdbc/archaiusJdbcPropertiesLoadingNoFileBackupTest.xml");
        
        // When
        DynamicStringProperty propertyFromArchaiusJdbc 
                = DynamicPropertyFactory.getInstance().getStringProperty(propertyArchaiusKey, expectedArchaiusPropertyValue);

        // Then
        assertThat(propertyFromArchaiusJdbc.get(), is(equalTo(expectedArchaiusPropertyValue)));

    }
}
