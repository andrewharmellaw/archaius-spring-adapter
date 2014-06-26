package com.capgemini.archaius.spring

import org.springframework.beans.factory.BeanCreationException
import org.springframework.context.support.ClassPathXmlApplicationContext
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

/**
 * @author: Gayathri Thiyagarajan
 * @version: 1.0
 */
@ActiveProfiles('default')
class SpringPropertiesMissingFileIsNotOkSpec extends Specification {

    def "missing spring properties files is not ok if IgnoreResourceNotFound property set to false" () {
        when:
            ctx = new ClassPathXmlApplicationContext('spring/springPropertiesMissingFileIsNotOKTest.xml')
        then:
            BeanCreationException bce = thrown()
            bce.cause.message == 'Failed properties: Property \'locations\' threw exception; nested exception is ' +
                    'java.lang.RuntimeException: Problem setting the locations.'
    }
}
