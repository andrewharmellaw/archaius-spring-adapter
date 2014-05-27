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
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ActiveProfiles;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

/**
 *
 * @author aharmel-law
 */
@ActiveProfiles("default")
public class SpringPropertiesMissingFileIsNotOKTest {
    
    @Test
    public void missingSpringPropertiesFilesIsNotOkIfIgnoreResourceNotFoundPropertySetToFalse() {
        
        try {
            new ClassPathXmlApplicationContext("spring/springPropertiesMissingFileIsNotOKTest.xml");
            fail("An exception should have been thrown when loading the context because the class path resource [META-INF/file-not-there.properties] cannot be resolved to a URL because it does not exist");
        } catch (BeanCreationException ex) { 
            assertThat(ex.getCause().getMessage(), is(equalTo("Failed properties: Property 'locations' threw exception; nested exception is java.lang.RuntimeException: Problem setting the locations.")));
        }
    }
}
