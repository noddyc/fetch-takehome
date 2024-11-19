FROM openjdk:22-jdk
ARG JAR_FILE=target/*.jar
COPY ./target/fetch-takehome-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]