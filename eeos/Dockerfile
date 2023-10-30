FROM openjdk:17-oracle
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src
RUN chmod +x ./gradlew
RUN ./gradlew build -x test
# 프로젝트 소스 폴더 복사
COPY src src
# gradlew 빌드
RUN ./gradlew bootjar
ARG JAR_FILE=/build/libs/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/*.jar"]