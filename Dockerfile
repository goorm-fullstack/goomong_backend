FROM openjdk:17-slim

WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src

RUN mkdir -p /root/.gradle

RUN echo "systemProp.http.proxyHost=krmp-proxy.9rum.cc\nsystemProp.http.proxyPort=3128\nsystemProp.https.proxyHost=krmp-proxy.9rum.cc\nsystemProp.https.proxyPort=3128" > /root/.gradle/gradle.properties

RUN chmod +x gradlew

RUN ./gradlew clean build

ENV DATABASE_URL=jdbc:mysql://mysqldb/krampoline

EXPOSE 8080

CMD ["java", "-jar", "/app/build/libs/goomong-0.0.1-SNAPSHOT.jar"]