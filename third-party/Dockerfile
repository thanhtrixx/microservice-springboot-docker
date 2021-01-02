FROM gradle:6.7.1-jdk8 AS gradle

COPY --chown=gradle:gradle src/ /build/src
COPY --chown=gradle:gradle build.gradle settings.gradle /build/
WORKDIR /build

RUN --mount=type=cache,target=root/.gradle/modules-2  gradle build --info

FROM openjdk:8-jre-slim

EXPOSE 8080
WORKDIR /app
COPY --from=gradle /build/build/libs/*.jar .

ENTRYPOINT ["java", "-jar", "/app/third-party-0.0.1-SNAPSHOT.jar"]
