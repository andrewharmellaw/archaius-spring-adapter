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

import org.apache.camel.CamelContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 *
 * @author aharmel-law
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:camel/camelPropertiesMissingFileIsOKTest.xml"})
@ActiveProfiles("default")
public class CamelPropertiesMissingFileIsOKTest {
    
    private final String camelPropertyKey = "{{var2}}";
    @Autowired
    @Qualifier("camel")
    protected CamelContext context;
    private String camelPropertyValue;
    @Value("${var2}") private String springPropertyValue;
    
    @DirtiesContext
    @Test
    public void camelPropertiesAreLoadedFromMultipleFilesInOrderAndAccessedViaTheCamelContext() throws Exception {
        
        camelPropertyValue = context.resolvePropertyPlaceholders(camelPropertyKey);
        
        assertThat("The context cannot be null.", context != null);
        assertThat(camelPropertyValue, is(equalTo("MY SECOND VAR")));
    }
    
    @Test
    public void missingSpringPropertiesFilesIsAlsoOkIfIgnoreResourceNotFoundPropertySetToTrue() {
        assertThat(springPropertyValue, is(equalTo("MY SECOND VAR")));
    }
}
