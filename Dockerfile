FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD . /
#ADD target/imageRecognition-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "target/imageRecognition-0.0.1-SNAPSHOT.jar"]
