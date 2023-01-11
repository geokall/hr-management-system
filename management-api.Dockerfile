FROM maven:3.8-jdk-11 as build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

FROM openjdk:11-jre-slim
COPY --from=build /home/app/target/management-api.jar /usr/local/lib/management-api.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/management-api.jar"]