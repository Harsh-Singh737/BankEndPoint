FROM eclipse-temurin

WORKDIR /app

COPY target/bankingendpoints-0.0.1-SNAPSHOT.jar bep.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "bep.jar"]