FROM openjdk:8-jdk
VOLUME /tmp
ADD target/fahrtenbuch-5.0.0.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
