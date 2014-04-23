Release Instructions
====================

Run the following Maven goals

```
mvn release:clean
mvn release:prepare
mvn release:perform
```

Credentials
-----------

Add the following server configuration to your Maven `settings.xml`

```
<server>
    <id>bintray</id>
    <username>bintray-user</username>
    <password>bintray-api-key</password>
</server>
```