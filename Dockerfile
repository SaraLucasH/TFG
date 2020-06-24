FROM openjdk:12-alpine

COPY DADM.jar /DADM.jar

CMD ["java","-jar","/DADM.jar"]