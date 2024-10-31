# 빌드 단계
FROM gradle:8.10.2-jdk17 AS build

WORKDIR /app

COPY . .

RUN gradle build --no-daemon -x test

# 실행 단계
FROM eclipse-temurin:17

WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

COPY entrypoint.sh entrypoint.sh

RUN chmod +x entrypoint.sh

EXPOSE 8080

ENTRYPOINT ["/app/entrypoint.sh"]
