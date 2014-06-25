package com.capgemini.archaius.spring.jdbc;

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
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.DynamicStringProperty;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 *
 * @author skumar81
 */
@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/jdbc/overLoadingPropertyFromFileWithJdbcTest.xml"})
@ActiveProfiles("default")
public class OverLoadingPropertyFromFileWithJdbcTest extends JdbcTestSuper {

    private final String propertyArchaiusKeyTwo = "Error404";
    private final String expectedArchaiusPropertyValueTwo = "Page not found";

    private final String propertyArchaiusKeyThree = "Error405";
    private final String expectedArchaiusPropertyValueThree = "Method Not Allowed from Property file";

    @Test
    public void propertiesAreLoadedFromDatabaseAndAccessedViaArchaiusDynamicStringProperty() throws InterruptedException {

        // value of Error is loaded from Database and overriding the value read from properties file.	
        DynamicStringProperty prop2 = DynamicPropertyFactory.getInstance().getStringProperty(propertyArchaiusKeyTwo, propertyArchaiusKeyTwo);

        assertThat(prop2.get(), is(equalTo(expectedArchaiusPropertyValueTwo)));

        // value of Errorcode is loaded from properties file as the same is not available in DB.
        DynamicStringProperty prop4 = DynamicPropertyFactory.getInstance().getStringProperty(propertyArchaiusKeyThree, propertyArchaiusKeyThree);

        assertThat(prop4.get(), is(equalTo(expectedArchaiusPropertyValueThree)));

    }

}
