FROM openjdk:17-jdk-slim

COPY target/banking-1.0-SNAPSHOT.jar banking-1.0-SNAPSHOT.jar

ENTRYPOINT ["java","-jar","/banking-1.0-SNAPSHOT.jar"]