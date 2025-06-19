# first stage: building the lab
from maven as build

workdir /app

copy pom.xml . 
copy common-api ./common-api
copy server-api ./server-api
copy server ./server
run mvn clean package assembly:single

# second stage: running the lab
from ubuntu/jre:21-24.04_stable as runtime

workdir /app

copy --from=build /app/server/target/8-server-1.0-jar-with-dependencies.jar /app

entrypoint ["java", "-jar", "8-server-1.0-jar-with-dependencies.jar"]
cmd []