FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY . .

RUN chmod +x gradlew

RUN ./gradlew bootJar --no-daemon

EXPOSE 8080

ENTRYPOINT ["java","-jar","build/libs/DentalCore-1.0.0.jar"]