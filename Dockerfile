FROM openjdk:11-jdk
WORKDIR /app
COPY target/scheduler-0.0.1-SNAPSHOT.jar /app/myapp.jar
EXPOSE 8080
CMD ["java", "-jar", "/app/myapp.jar"]
