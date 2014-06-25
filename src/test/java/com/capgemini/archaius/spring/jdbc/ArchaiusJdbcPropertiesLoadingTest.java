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
public class ArchaiusJdbcPropertiesLoadingTest extends JdbcTestSuper {

    private final String propertyArchaiusKeyTwo = "Error404";
    private final String expectedArchaiusPropertyValueTwo = "Page not found";

    private final String propertyArchaiusKeyThree = "Error500";
    private final String expectedArchaiusPropertyValueThree = "Internal Server Error";

    private final String newArchaiusPropertyKeyTwo = "Error404";
    private final String newExpectedArchaiusPropertyValueTwo = "New Page not found";

    private final String newArchaiusPropertyKeyThree = "Error500";
    private final String newExpectedArchaiusPropertyValueThree = "New Internal Server Error";

    public Logger LOGGER = LoggerFactory.getLogger(ArchaiusJdbcPropertiesLoadingTest.class);

    @Test
    public void propertiesAreLoadedFromDatabaseAndAccessedViaArchaiusDynamicStringProperty() throws InterruptedException {

        // when  initial value at set in DB
        LOGGER.info("runnig test for initial values");
        // then  initial value should be retrieved from DB.
        DynamicStringProperty prop2 = DynamicPropertyFactory.getInstance().getStringProperty(propertyArchaiusKeyTwo, propertyArchaiusKeyTwo);

        assertThat(prop2.get(), is(equalTo(expectedArchaiusPropertyValueTwo)));

        DynamicStringProperty prop3 = DynamicPropertyFactory.getInstance().getStringProperty(propertyArchaiusKeyThree, propertyArchaiusKeyThree);

        assertThat(prop3.get(), is(equalTo(expectedArchaiusPropertyValueThree)));

        // when  updated the value in db
        UpdateTestDataForArchaiusTest updateTestData = new UpdateTestDataForArchaiusTest();
        updateTestData.initializedDerby();
        Thread.sleep(100);

        LOGGER.info("runnig test for updated values");
        // then   new value should be reflected.
        prop2 = DynamicPropertyFactory.getInstance().getStringProperty(newArchaiusPropertyKeyTwo, newArchaiusPropertyKeyTwo);

        assertThat(prop2.get(), is(equalTo(newExpectedArchaiusPropertyValueTwo)));

        prop3 = DynamicPropertyFactory.getInstance().getStringProperty(newArchaiusPropertyKeyThree, newArchaiusPropertyKeyThree);

        assertThat(prop3.get(), is(equalTo(newExpectedArchaiusPropertyValueThree)));

    }

}
