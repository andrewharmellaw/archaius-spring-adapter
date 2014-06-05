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
package com.capgemini.archaius.spring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Properties;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 *
 * @author aharmel-law
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/springPropertiesLoadingTest.xml"})
@ActiveProfiles("default")
public class SpringPropertiesLoadingTest {

    // TODO Spring @Configuration tests
    // TODO Spring @Environment.getProperty tests
    
    private final String propertyKey = "var2";
    private final String expectedPropertyValue = "MY SECOND VAR";
    @Value("${" + propertyKey + "}") private String propertyValue;
        
    @Test
    public void springPropertiesAreLoadedFromSingleFileAndAccessedViaTheSpringValueAnnotation() {
        assertThat(propertyValue, equalTo(expectedPropertyValue));
    }
    
    /**
     * Of course this works as we're just testing Spring - instead we need to document that you can't do this any more
     * as it will ignore Archaius changes.
     * 
     * @throws Exception
     */
    @Test
    public void springPropertiesAreLoadedFromSingleFileAndAccessedViaPropertiesLoadersUtils() throws Exception {
        
        Resource resource = new ClassPathResource("properties/system.properties");
        Properties props = PropertiesLoaderUtils.loadProperties(resource);
        
        assertThat(props.containsKey(propertyKey), is(true));
    }
}
