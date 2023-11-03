FROM openjdk:17-oracle
RUN mkdir -p /logs

ENV	PROFILE defaultgi
ENV TZ=Asia/Seoul

ARG JAVA_OPTS

ARG RELEASE_VERSION
ENV DD_VERSION=${RELEASE_VERSION}

ARG JAR_FILE="./build/libs/*.jar"
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]