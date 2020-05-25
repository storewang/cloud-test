FROM openjdk:8-jdk-alpine
VOLUME /tmp

COPY docker-entrypoint.sh /
ENV profiles=dev \
    serverPort=8080 \
    appname=app.jar
WORKDIR /
RUN chmod 777 docker-entrypoint.sh
RUN mkdir /logs && chmod 777 -R /logs
EXPOSE 8080 8719

ENTRYPOINT ["/docker-entrypoint.sh"]