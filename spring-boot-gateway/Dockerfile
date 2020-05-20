FROM yiyun8/openjdk:8-mic-v10
VOLUME /tmp
ARG JAR_FILE
ADD ${JAR_FILE} app.jar
ENV appname=app.jar
WORKDIR /
EXPOSE 8080 8719
