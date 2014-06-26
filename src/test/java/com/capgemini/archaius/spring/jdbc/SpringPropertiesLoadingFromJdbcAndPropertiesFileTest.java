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

import java.util.Properties;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
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
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/jdbc/springPropertiesLoadingFromJdbcAndPropertiesFileTest.xml"})
@ActiveProfiles("default")
public class SpringPropertiesLoadingFromJdbcAndPropertiesFileTest extends AbstractArchaiusJdbcTest {

    private final String propertyKey = "var2";
    @Value("${" + propertyKey + "}") private String propertyValue;
    private final String expectedPropertyValue = "MY SECOND VAR";
    
    private final String propertyArchaiusKey = "Error404";
    @Value("${" + propertyArchaiusKey + "}") private String propertyArchaiusValue;
    private final String expectedSpringPropertyArchaiusValue = "Page not found";
    
    @Test
    public void propertiesAreLoadedFromDatabaseAndAccessedViaTheSpringValueAnnotation() {
        assertThat(propertyArchaiusValue, equalTo(expectedSpringPropertyArchaiusValue));
    }

    @Test
    public void springPropertiesAreLoadedFromSingleFileAndAccessedViaTheSpringValueAnnotation() {
        assertThat(propertyValue, equalTo(expectedPropertyValue));
    }

    /**
     * Of course this works as we're just testing Spring - instead we need to
     * document that you can't do this any more as it will ignore Archaius
     * changes.
     *
     * @throws Exception
     */
    @Test
    public void springPropertiesAreLoadedFromSingleFileAndAccessedViaPropertiesLoadersUtils() throws Exception {

        Resource resource = new ClassPathResource("properties/archaiusSystem.properties");
        Properties props = PropertiesLoaderUtils.loadProperties(resource);

        assertThat(props.containsKey(propertyKey), is(true));
    }
    
    @Test
    public void propertiesAreLoadedFromDatabaseAndAccessedViaTheSpringValueAnnotationButAreNotDynamic() throws InterruptedException {

        assertThat(propertyArchaiusValue, equalTo(expectedSpringPropertyArchaiusValue));

        // when updating the data in DB
        ArchaiusPropertyDataUpdater.updatePropertyData();

        //then  still spring context will have old data not the new values
        assertThat(propertyValue, equalTo(expectedPropertyValue));
    }
}
