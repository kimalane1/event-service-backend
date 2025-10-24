FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

COPY gradlew gradlew
COPY gradle gradle
COPY build.gradle* settings.gradle* ./

RUN chmod +x gradlew
RUN ./gradlew dependencies --no-daemon || true

COPY . .

RUN ./gradlew clean bootJar --no-daemon

FROM eclipse-temurin:21-jdk
WORKDIR /app

COPY --from=build /app/build/libs/*.jar application.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","application.jar"]