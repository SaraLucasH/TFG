FROM store/oracle/jdk:11

COPY target/TFG-0.0.1-SNAPSHOT-jar-with-dependencies.jar /dadm.jar

CMD ["java","-jar","/dadm.jar"]