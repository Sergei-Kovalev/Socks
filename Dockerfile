# Build
FROM gradle:7.5.1-jdk17 AS builder
WORKDIR /usr/src/socks
COPY . .
RUN chown -R gradle:gradle /usr/src/socks
RUN gradle clean build -x test

# Package
FROM openjdk:17-slim-buster
WORKDIR /app
COPY --from=builder /usr/src/socks/build/libs/Socks*.jar /app/Socks.jar
EXPOSE 9009
ENTRYPOINT ["java", "-jar", "/app/Socks.jar"]