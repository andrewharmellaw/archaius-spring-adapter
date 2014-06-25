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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import com.capgemini.archaius.spring.jdbc.dataload.UpdateTestDataForArchaiusTest;

/**
 *
 * @author Sanjay Kumar
 */
@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/jdbc/springJdbcPropertiesLoadingTest.xml"})
@ActiveProfiles("default")
public class SpringJdbcPropertiesLoadingTest extends JdbcTestSuper {

    private final String propertySpringKey = "Error404";
    private final String expectedSpringropertyValue = "Page not found";
    @Value("${" + propertySpringKey + "}")
    private String propertyValue;

    @Test
    public void propertiesAreLoadedFromDatabaseAndAccessedViaTheSpringValueAnnotation() throws InterruptedException {

        //property loaded at startup.
        assertThat(propertyValue, equalTo(expectedSpringropertyValue));

        // when updating the data in DB
        UpdateTestDataForArchaiusTest updateTestData = new UpdateTestDataForArchaiusTest();
        updateTestData.initializedDerby();

        //then  still spring context will have old data not the new values
        assertThat(propertyValue, equalTo(expectedSpringropertyValue));
    
    }
    
}
