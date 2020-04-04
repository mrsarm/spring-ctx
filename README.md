Spring Context
==============

`spring-ctx` contains the Java class `ctx.App`, that
exposes the Spring context statically.

You can get a bean object from the context like this in **Java**, without the
need to inject the bean into your class:

```java
MyUserService myUserService = ctx.App.getBean(MyUserService.class);
```

But the most important feature is to use it with the
[jshell](https://docs.oracle.com/javase/9/jshell/introduction-jshell.htm) tool
included in Java 9+ distributions, to access within the console
to the Spring context, and therefore all the business objects created with it,
like many other frameworks allows to do, eg. the *Grails Console* in Groovy + Grails,
the *Django Admin Shell* in Python + Django, and the *Rails Console* in Ruby + RoR.

To do so, you need to start first a jshell console, start running
your application, and then use the `ctx.App` class to access your
bean objects.

> :information_source: Take a look to [spring-ctx-groovy](https://github.com/grayshirts/spring-ctx-groovy)
> for the same class but implemented in Groovy (not exactly the same class though).

Also exposes the properties of the project with the `prop` static method:

```bash
jshell> ctx.App.getProp("server.context-path")
$10 ==> "/api"
```

When an object is returned, the jshell prints a representation of the
object (it calls the `toString()` method), but sometimes it's not the
best way to read the result, or you just need a JSON representation,
in that case you can ask to the `ctx.App` class to get the
[ObjectMapper](https://docs.spring.io/spring-boot/docs/current/reference/html/howto-spring-mvc.html#howto-customize-the-jackson-objectmapper)
used by your application to print out results in JSON format:

```bash
jshell> var objMapper = ctx.App.getObjectMapper()
objMapper ==> ObjectMapper

jshell> System.out.println(objMapper.writeValueAsString(person))
{"name":"John","lastName":"Doe","age":null}
```

Or you can call the convenient methods `pjson(Object)` and `ppson(Object)`
(pretty print version) that allow to print the object using
the same object mapper mentioned above and then terminate the line:

```bash
jshell> ctx.App.ppjson(person)
{
  "name" : "Jhon",
  "lastName" : "Due",
  "age" : null
}
```

You can also access to the the Spring context with `ctx.App.getContext()`, it
returns a [ApplicationContext](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/context/ApplicationContext.html)
instance, but the `App` class provides enough static methods to get
the bean objects, configuration properties, access the object mapper
and the current environment active (profiles).


Configuration
-------------

To add this library to your project, depending of your building
tool, these are the settings needed:

### Gradle

Add the following configuration to the `build.gradle` file
of your project:

1. `dependencies` section:

   ```groovy
   implementation 'com.github.mrsarm:spring-ctx:1.0.0'
   ```

2. And at the end of the `repositories` section:

   ```groovy
   maven { url 'https://jitpack.io' }
   ```

### Maven

Add the following configuration to the `pom.xml` file
of your project:

1. `dependencies` section:

   ```xml
   <dependency>
       <groupId>com.github.mrsarm</groupId>
       <artifactId>spring-ctx</artifactId>
       <version>1.0.0</version>
   </dependency>
   ```

2. And at the end of the `repositories` section:

   ```xml
   <repository>
       <id>jitpack.io</id>
       <url>https://jitpack.io</url>
   </repository>
   ```


System Requirements
-------------------

 * JDK 7+


Build & Publish
---------------

Compile and build the .jar locally with:

```bash
$ ./gradlew build
```

Publish to your local Maven repo:

```bash
$ ./gradlew publishToMavenLocal
```

Publish to the [JitPack](https://jitpack.io/) public repository:
just release a new tag in the repository, and _JitPack_ will do
the magic !!


About
-----

Project: https://github.com/mrsarm/spring-ctx

Author: Mariano Ruiz <mrsarm@gmail.com>

License: [Apache Software License 2.0](https://www.apache.org/licenses/LICENSE-2.0).
