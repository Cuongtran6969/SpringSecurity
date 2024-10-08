#FROM ubuntu:latest
#LABEL authors="admin"
#
#ENTRYPOINT ["top", "-b"]
FROM openjdk:17

ARG FILE_JAR=