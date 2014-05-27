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

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 *
 * @author aharmel-law
 */
@ActiveProfiles("default")
public class CamelPropertiesMissingFileIsNotOKTest {
    
    @Test
    public void missingSpringPropertiesFilesIsNotOkIfIgnoreResourceNotFoundPropertySetToFalse() {
        // Load the context
        try {
            new ClassPathXmlApplicationContext("camel/camelPropertiesMissingFileIsNotOKTest.xml");
            fail("An exception should have been thrown when loading the context because the class path resource [META-INF/file-not-there.properties] cannot be resolved to a URL because it does not exist");
        } catch (BeanCreationException ex) { 
            assertTrue(ex.getMessage().startsWith("Error creating bean with name 'com.capgemini.archaius.spring.ArchaiusBridgePropertyPlaceholderConfigurer#0' defined in class path resource [camel/camelPropertiesMissingFileIsNotOKTest.xml]: Error setting property values"));
        } 
    }
}
