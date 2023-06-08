FROM openjdk:8-jdk-alpine
RUN apk --no-cache add curl
RUN curl -u admin:nexus -o espritclub.jar "http://192.168.2.105:8081/repository/maven-snapshots/com/espritclub/spring/espritclub/1.0-SNAPSHOT/espritclub-1.0-20230608.195607-2.jar" -L
ENTRYPOINT ["java","-jar","/espritclub.jar"]
EXPOSE 8089