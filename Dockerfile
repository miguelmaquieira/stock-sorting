# Stage 1: Build the application
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/stock-sorting-impl/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT java -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE} -Dlogging.level.com.mgm.stocksorting=${LOGGING_LEVEL} -jar app.jar

