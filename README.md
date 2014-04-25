[![Build Status](https://buildhive.cloudbees.com/job/Capgemini/job/archaius-spring-adapter/badge/icon)](https://buildhive.cloudbees.com/job/Capgemini/job/archaius-spring-adapter/)

Archaius Spring Adapter
===============================

Welcome to the archaius-spring-adapter. As we say in the POM, our aim is to 
simply extend the Spring (and Camel) PropertyPlaceholders in order to support 
Netflix's Archaius as the single of all property information.

Why would we go to this trouble?  Properties can be loaded fine into Spring, and
now, with the BridgePropertyPlaceholder from Camel you can use that same file 
for your Camel properties too.

But what if you want more? Specifically in our case, our itch that we scratched
was the desire to use the Netflix Hystrix circuit breakers in our Spring/Camel 
projects.  These depend upon Archaius for their config, and as you need to tune
them, this config is important.  Having already got all our Camel and Spring
properties in one place, why now accept another properties source just for one 
more component in our architecture. The archaius-spring-adapter was born.

Finally, it should be noted right up front that while the itch for this 
scratching did originate with us, we weren't alone. The initial example upon 
which this code is based can be seen in this Archaius issue thread: 

https://github.com/Netflix/archaius/issues/113

We'd like to thank the guys there for their support, and permission to release 
this code licensed under the Apache v2.0 OSS licence.

Get Started
-----------

It's dead simple to use the adapter. Just fork the project to your GitHub 
account (handy if you want to make a pull request later) and then clone it 
locally (we're not yet in a Maven repo, sorry).  Then simply run:

    mvn clean install

Now you're free to start bringing in the archaius goodness.  We're (still) using
Spring XML config and all you need do if you have Spring alone (i.e. no Camel)
is something like:

    <?xml version="1.0" encoding="UTF-8"?>
    <beans xmlns="http://www.springframework.org/schema/beans"
           xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xmlns:context="http://www.springframework.org/schema/context"
           xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
                               http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-3.1.xsd">

        <!-- Config loading via Spring-Archaius-->
        <bean class="com.capgemini.archaius.spring.ArchaiusPropertyPlaceholderConfigurer">
            <property name="location" value="classpath:/META-INF/system.properties" />
        </bean>

    </beans>

The "location" property works just as you would expect it to.  By this we mean 
you can have an ordered list of properties files:

    <!-- Config loading via Spring-Archaius-->
    <bean class="com.capgemini.archaius.spring.ArchaiusPropertyPlaceholderConfigurer">
        <property name="locations">
            <list>
                <value>classpath:/META-INF/system.properties</value>
                <value>classpath:/META-INF/even-more-system.properties</value>
            </list>
        </property>
    </bean>

With property overloading as you'd expect.

Finally you can let Spring know whether it can ignore missing resource files:

    <bean class="com.capgemini.archaius.spring.ArchaiusPropertyPlaceholderConfigurer">
        <property name="ignoreResourceNotFound" value="false" />
        <property name="locations">
            <list>
                <value>classpath:/META-INF/system.properties</value>
                <value>classpath:/META-INF/file-not-there.properties</value>
            </list>
        </property>
    </bean>

If you're using Camel, everything in the above examples works as you'd expect, 
but you need to use a different bean class:

    <?xml version="1.0" encoding="UTF-8"?>
    <beans xmlns="http://www.springframework.org/schema/beans"
           xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xmlns:context="http://www.springframework.org/schema/context"
           xmlns:camel="http://camel.apache.org/schema/spring"
           xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
                               http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd
                               http://camel.apache.org/schema/spring http://camel.apache.org/schema/spring/camel-spring-2.12.2.xsd">

        <!-- Config loading via Spring-Archaius-->
        <bean class="com.capgemini.archaius.spring.ArchaiusBridgePropertyPlaceholderConfigurer">
            <property name="locations">
                <list>
                    <value>classpath:/META-INF/system.properties</value>
                    <value>classpath:/META-INF/file-not-there.properties</value>
                </list>
            </property>
            <property name="ignoreResourceNotFound" value="true" />
        </bean>

        <camel:camelContext id="camel" />

    </beans>

That's it!

Getting Involved
----------------

We're clearly sorting out support for our own problems first, but its clear that 
there is much more we could add to this project.  We'd love to have
contributions from folks in all the standard ways:

1. Questions and Answers via the Google Group
1. Issue Reports via GitHub
1. Pull Requests (fixes, more tests, new features, typo-corrections etc) via GitHub 
(we follow the standard workflow)
1. Wiki documentation

We maintain a list of the issues that we're working on as you'd expect.  By
looking there you can see what our priorities are.  Please feel free to comment
on any of them or add more.

Developers
----------

* [Andrew Harmel-Law](https://github.com/andrewharmellaw)
* [Gaythu Rajan](https://github.com/gaythu-rajan)
* [Nick Walter](https://github.com/nickjwalter)
* [Russell Hart](https://github.com/rhart)
