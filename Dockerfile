#FROM ubuntu:latest
#LABEL authors="admin"
#
#ENTRYPOINT ["top", "-b"]
FROM openjdk:17

ARG FILE_JAR=target/SpringSecurity-0.0.1-SNAPSHOT.jar

ADD ${FILE_JAR} api-service.jar

ENTRYPOINT ["java", "-jar", "api-service.jar"]
#//DOCKER chay doc lap, chay mang rieng, cho phep truy xuat conatainer trong ung dung
EXPOSE 8080