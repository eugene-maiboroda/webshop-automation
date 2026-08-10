FROM --platform=linux/amd64 maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /build
COPY pom.xml ./
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests


FROM --platform=linux/amd64 eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build \
 /build/target/webshop-automation*.jar \
    webshop-automation.jar
EXPOSE 3344
CMD ["java", "-jar", "webshop-automation.jar"]