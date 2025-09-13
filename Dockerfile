FROM amazoncorretto:17-alpine-jdk
LABEL authors="Oscar"
WORKDIR /app
EXPOSE 8080
COPY ./target/msvc-tesis-0.0.1-SNAPSHOT.jar msvc-tesis
ENTRYPOINT ["java", "-jar" , "msvc-tesis"]