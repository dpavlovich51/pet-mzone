FROM openjdk:16
COPY ./target/main-0.0.1-SNAPSHOT.jar /opt/app/
WORKDIR /opt/app/
EXPOSE 8080
CMD java -jar main-0.0.1-SNAPSHOT.jar