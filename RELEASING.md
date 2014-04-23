Release Instructions
====================

Run `mvn release:clean`
Run `mvn release:prepare` n.b. do not accept the default version numbers or tag name.
Run `mvn release:perform`

Visit https://bintray.com/capgeminiuk/maven/archaius-spring-adapter/view and publish the new version using the `publish` link.

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