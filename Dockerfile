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

CMD ["java", "-Dhttp.proxyHost=krmp-proxy.9rum.cc", "-Dhttp.proxyPort=3128","-Dhttps.proxyHost=krmp-proxy.9rum.cc", "-Dhttps.proxyPort=3128", "-jar", "-Dspring.profiles.active=prod", "/app/build/libs/goomong-0.0.1-SNAPSHOT.jar"]