FROM openjdk:11
ADD target/pi-workbench-service-0.0.1-SNAPSHOT.jar pi-workbench-service.jar
EXPOSE 8082
ENTRYPOINT ["java","-jar","pi-workbench-service.jar"]