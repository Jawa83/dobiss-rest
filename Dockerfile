# (experimental) docker feature 'buildx' can be used for multi-CPU architecure image builds
# https://docs.docker.com/docker-for-mac/multi-arch/
# docker buildx build --platform linux/amd64,linux/arm/v7 -t wardjanssens/dobiss-rest:latest --push .
#
# Docker hub does not support this (yet):
# https://github.com/docker/hub-feedback/issues/1874
FROM openjdk:11.0.3-jre-slim
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]