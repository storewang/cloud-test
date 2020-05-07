FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG JAR_FILE
ADD ${JAR_FILE} app.jar
ENV profiles dev
EXPOSE 8080
ENTRYPOINT ["java","-Dspring.profiles.active=${profiles} -Djava.security.egd=file:/dev/urandom","-jar","/app.jar"]