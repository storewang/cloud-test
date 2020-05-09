FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG JAR_FILE
ADD ${JAR_FILE} app.jar
ENV profiles dev
EXPOSE 8899 8719
ENTRYPOINT ["java","-Dspring.profiles.active=dev","-Djava.security.egd=file:/dev/urandom","-jar","/app.jar"]