FROM maven:3.6.0-jdk-8-alpine

COPY . /uncertain_inference

WORKDIR /uncertain_inference
RUN mvn clean install -U

CMD ["java", "-jar", "core/target/uncertain-inference-jar-with-dependencies.jar", "-service", "-address", "db-service:8080"]