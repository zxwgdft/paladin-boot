FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY target/spring-boot-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8900
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
