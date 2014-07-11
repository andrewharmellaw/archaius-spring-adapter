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
package com.capgemini.archaius.camel

import org.springframework.beans.factory.BeanCreationException
import org.springframework.context.support.ClassPathXmlApplicationContext
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

/**
 * @author: Gayathri Thiyagarajan
 * @version: 1.0
 */
@ActiveProfiles('default')
class CamelPropertiesMissingFileIsNotOKSpec extends Specification {

    def"missing camel properties files is not ok if IgnoreResourceNotFound property set to false"() {
        when:
            ctx = new ClassPathXmlApplicationContext('camel/camelPropertiesMissingFileIsNotOKTest.xml')
        then:
            BeanCreationException bce = thrown()
            bce.cause.message == 'Failed properties: Property \'locations\' threw exception; nested exception ' +
                'is java.lang.RuntimeException: Problem setting the locations.'
    }

}
