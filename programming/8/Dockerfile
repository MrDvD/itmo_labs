FROM bellsoft/liberica-openjdk-alpine

WORKDIR /app
COPY server/target/8-server-1.0-jar-with-dependencies.jar /app

ENTRYPOINT ["java", "-jar", "8-server-1.0-jar-with-dependencies.jar"]
CMD []