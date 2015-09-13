# Payment Highway Example with Java Spring

This is a simple Java Spring example to demonstrate how to use the Payment Highway Java Library.

The project is pre-configured to use the Payment Highway development sandbox environment.

Payment Highway Documentation: https://paymenthighway.fi/dev/
Java Lib Source: https://github.com/solinor/paymenthighway-java-lib
Maven Repository: "https://oss.sonatype.org/content/repositories/snapshots"
Package: "io.paymenthighway:paymenthighway:1.0-SNAPSHOT"

# Requirements
- An internet connection for dependencies and running the Application
- Java 8
- Optionally (recommended): An IDE such as IntelliJ Idea (https://www.jetbrains.com/idea/) or Eclipse (https://eclipse.org/)
- Optionally: Maven or Maven plugin in IDEA (however gradle binaries is included) 

# Structure 

* `initial`

Contains a tutorial version of this example. Follow instructional comments in the controllers and build up your application!

* `complete`

Contains a complete working example.
 
# Importing project to IDE

You can use the `pom.xml` to import the example as a Maven project, or the `gradle.build` as Gradle.
Both files can be found from the `initial` and the `complete` directories.

# Running

By default the project is configured to run at `http://localhost:8080`, but the port can be changed in `resources/application.properties`

IDEs:
Import the project and add the run Application configuration if it does not already exist.

Gradle:
`./gradlew bootRun`

Maven:
`mvn spring-boot:run`

More Info:
http://docs.spring.io/spring-boot/docs/current/reference/html/using-boot-running-your-application.html

# Working with the tutorial


It is recommended to first have a look at the Payment Highway Documentation page and the Java Library readme in Github. The library has some examples which can be used in this tutorial.

Once you have imported the `initial` project, you can start filling in the missing parts. You may for example start with the controller.FormAddCardController class. At any time you can run the application and try it out.


# Help us make it better

Please tell us how we can make the example better. If you have a specific feature request or if you found a bug, please use GitHub issues. You may also fork the project and send a pull request with the improvements!

