#### Stage 1: Build the application
FROM openjdk:12-jdk-alpine

RUN /bin/sh -c "apk add --no-cache bash"

# Set the current working directory inside the image
WORKDIR /app

COPY target/MessageProvider-1.0.jar MessageProvider-1.0.jar

ENTRYPOINT ["java","-jar","MessageProvider-1.0.jar"]
