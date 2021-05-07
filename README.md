# Overview
A better place to keep your artifact deployment credentials (than keeping them with the source code in your 
version control system or probably in your build system creating vendor lock-in)

# Resources

## Testing
* [Mockito](<https://site.mockito.org>)
* [REST Clients](<https://stackoverflow.com/questions/221442/how-do-you-create-a-rest-client-for-java>)
* [RESTEasy Client](<https://docs.jboss.org/resteasy/docs/3.11.4.Final/userguide/html_single/index.html#RESTEasy_Client_Framework>)
* [Java Garbage Collectors](<https://dzone.com/articles/choosing-the-best-garbage-collection-algorithm-for>)
* [](<>)
* [](<>)

## Tomcat
* [Tomcat](<https://tomcat.apache.org/tomcat-9.0-doc/index.html>)
* [Tomcat Config](<https://tomcat.apache.org/tomcat-9.0-doc/config/>)

## Other
* [Java Memory Tuning](<https://nodramadevops.com/2019/10/docker-memory-resource-limits/>)
* [Hibernate](<https://docs.jboss.org/hibernate/validator/6.2/reference/en-US/html_single/#section-integration-with-cdi>)
* [JAX-RS (Wikipedia)](<https://en.wikipedia.org/wiki/Jakarta_RESTful_Web_Services>)
* [RESTEasy](<https://resteasy.github.io>)
* [Minica CA](<https://github.com/jsha/minica>)
* [Top 10 Web Application Security Risks](<https://owasp.org/www-project-top-ten/>)
* [Active Object using ForkJoinPool](<https://en.wikipedia.org/wiki/Active_object#Java_8_(alternative)>)
* [Software design pattern](<https://en.wikipedia.org/wiki/Software_design_pattern>)
* [Java Validation API Example](<https://www.baeldung.com/javax-validation>)
* [Logback MDC](<http://logback.qos.ch/manual/mdc.html>)
* [Logback Rolling File Appender](<http://logback.qos.ch/manual/appenders.html>)
* [Java Blocking Queue](<https://www.baeldung.com/java-blocking-queue>)
* [Java Security](<https://docs.oracle.com/en/java/javase/11/security/security-api-specification1.html>)
* [Security Terms Explained](<https://docs.oracle.com/en/java/javase/11/security/java-cryptography-architecture-jca-reference-guide.html#GUID-94225C88-F2F1-44D1-A781-1DD9D5094566>)
* [Slf4j Simple Logger](<http://www.slf4j.org/api/org/slf4j/impl/SimpleLogger.html>)
* [Github Secrets](<https://docs.github.com/en/actions/reference/encrypted-secrets>)
* [Open Web Beans](<http://openwebbeans.apache.org>)
* [CDI Spec](<http://cdi-spec.org>)
* [RESTEasy Docs](<https://docs.jboss.org/resteasy/docs/3.11.4.Final/userguide/html_single/>)
* [CDI Tutorial](<https://docs.oracle.com/javaee/6/tutorial/doc/gjebj.html>)
* [Java EE CDI](<https://www.baeldung.com/java-ee-cdi>)
* [MapDB](<https://jankotek.gitbooks.io/mapdb/content/quick-start/>)
* [AES Encryption](<https://www.baeldung.com/java-aes-encryption-decryption>)
* [Java Cryptography Architecture (JCA) Reference Guide](<https://docs.oracle.com/javase/8/docs/technotes/guides/security/crypto/CryptoSpec.html>)
* [Java PKI Programmer's Guide](<https://docs.oracle.com/javase/8/docs/technotes/guides/security/certpath/CertPathProgGuide.html>)
* [Reflect Tutorial](<https://docs.oracle.com/javase/tutorial/reflect/member/fieldValues.html>)
* [Apache Ant](<https://ant.apache.org/manual/index.html>)

# Deployment
* Create a folder for the `db` and a folder for the `log` as owner 11001 and group root (give the group rx access: chmod g+rx)
* Make sure the docker run command maps to these folders (see the s2i-dcv-test/Makefile for an example)
* The files in the db folder needs to be backed up while the docker container is stopped.
* The files in the log folder need to be archived and cleaned up so as to ensure that the disk does not fill up.
* Use `monit` (or another system monitor) to alert on disk full and errors in the log files.

# Notes
* The .keystore file in the project folder (used for testing), has a password equal to the filename 
* Use Ubuntu signed installs only for enhanced security
* For enhanced security, see https://www.tecmint.com/protect-apache-using-mod_security-and-mod_evasive-on-rhel-centos-fedora/
* to convert key.pem for use in Java: `openssl pkcs8 -topk8 -inform PEM -outform DER -in private_key_file  -nocrypt > pkcs8_ke`
* to add minica CA for testing: `keytool -importcert -file /tmp/minica.pem -alias minica -keystore src/test/resources/cacerts`

# TODO
* Save AJP and keystore and shutdown password in keystore
* Use @Notnull annotations
* Limit secret key and value sizes
* Write a standalone app to import cert key into .keystore
* Implement delete entry
* checkout org.apache.catalina.startup. EXIT_ON_INIT_FAILURE (https://tomcat.apache.org/tomcat-9.0-doc/config/systemprops.html)
* MapDB needs compaction sometimes. Run DB.compact() or see background compaction options.
* Encrypt/Decrypt mapdb entries
* change createOrOpen() to just open()
* MDC
