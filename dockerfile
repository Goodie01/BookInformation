FROM maven:3.6.3-jdk-11
COPY app/ /app/
WORKDIR /app
RUN mvn package

FROM openjdk:11.0.5-jre
WORKDIR /app
COPY --from=0 /app/target/bookInformation.jar /app
CMD ["java", "-Dprop=/app/default.properties", "-jar /app/bookInformation.jar"]