# Overview
A better place to keep your artifact deployment credentials (than keeping them with the source code in your 
version control system)

# Resources
* [Official Apache Maven documentation](<https://maven.apache.org/guides/index.html>)

## Spring
* [Sprint Boot](<https://docs.spring.io/spring-boot/docs/2.4.3/reference/html/index.html>)
* [Spring Boot Maven Plugin Reference Guide](<https://docs.spring.io/spring-boot/docs/2.4.3/maven-plugin/reference/html/>)
* [Spring Framework](<https://docs.spring.io/spring-framework/docs/5.3.4/reference/html/index.html>)
* [Traditional Deployment](<https://docs.spring.io/spring-boot/docs/2.4.3/reference/html/howto.html#howto-traditional-deployment>)
* [Spring Core](<https://docs.spring.io/spring-framework/docs/5.3.4/reference/html/core.html#spring-core>)
* [Spring MVC](<https://docs.spring.io/spring-framework/docs/5.3.4/reference/html/web.html#spring-web>)
* [Spring Lifecycle and Events](<https://docs.spring.io/spring-framework/docs/5.3.4/reference/html/core.html#context-functionality-events>)
* [Spring Task Scheduling](<https://docs.spring.io/spring-framework/docs/5.3.4/reference/html/integration.html#scheduling>)
* [Spring Security](<https://docs.spring.io/spring-security/site/docs/5.4.5/reference/html5/>)

## Testing
* [Spring Testing](<https://docs.spring.io/spring-framework/docs/5.3.4/reference/html/testing.html#testing>)
* [Tomcat Embedded](<https://tomcat.apache.org/tomcat-5.5-doc/catalina/docs/api/org/apache/catalina/startup/Embedded.html>)
* [How to test REST Controllers outside container](<https://github.com/spring-projects/spring-framework/blob/master/spring-test/src/test/java/org/springframework/test/web/servlet/samples/client/standalone/ResponseBodyTests.java>)
* [Arquillian Docs](<http://arquillian.org/arquillian-core/#_guide>)
* [](<>)
* [](<>)

## Tomcat
* [Tomcat](<https://tomcat.apache.org/tomcat-9.0-doc/index.html>)
* [Tomcat Config](<https://tomcat.apache.org/tomcat-9.0-doc/config/>)

## Other
* [Top 10 Web Application Security Risks](<https://owasp.org/www-project-top-ten/>)
* [Active Object using ForkJoinPool](<https://en.wikipedia.org/wiki/Active_object#Java_8_(alternative)>)
* [Software design pattern](<https://en.wikipedia.org/wiki/Software_design_pattern>)
* [Java Validation API Example](<https://www.baeldung.com/javax-validation>)
* [Servlet Handler Interceptor Example](<https://www.baeldung.com/spring-mvc-handlerinterceptor>)
* [Logback MDC](<http://logback.qos.ch/manual/mdc.html>)
* [Java Blocking Queue](<https://www.baeldung.com/java-blocking-queue>)
* [Java Security](<https://docs.oracle.com/en/java/javase/11/security/security-api-specification1.html>)
* [Security Terms Explained](<https://docs.oracle.com/en/java/javase/11/security/java-cryptography-architecture-jca-reference-guide.html#GUID-94225C88-F2F1-44D1-A781-1DD9D5094566>)
* [Slf4j Simple Logger](<http://www.slf4j.org/api/org/slf4j/impl/SimpleLogger.html>)

# Notes
* Keystore format/type uses: "pkcs11"
* Secret key entries use algorithm: "PBEWithMD5AndDES"
* The .keystore file in the project folder, and the keystore file in src/test/resources have a password equal to the filename 
* Before running the application locally or the tests, run `export KEYSTORE_PASSWORD=.keystore`

# TODO
* Implement caching
* Save AJP and keystore and shutdown password in keystore
* Use Spring @Notnull
* OWASP top 10
* Ubuntu signed installs only 
* https://www.tecmint.com/protect-apache-using-mod_security-and-mod_evasive-on-rhel-centos-fedora/
