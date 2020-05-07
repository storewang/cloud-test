FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG JAR_FILE
ADD ${JAR_FILE} app.jar
# ENV profiles dev
# ENV nacos.standalone true
EXPOSE 8848
ENTRYPOINT ["java","-Dnacos.standalone=true -Djava.security.egd=file:/dev/urandom","-jar","/app.jar"]