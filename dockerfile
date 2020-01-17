FROM maven:3.6.3-jdk-11
COPY app/ /app/
WORKDIR /app
RUN mvn package