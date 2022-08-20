FROM openjdk:17-alpine
RUN mkdir -p /opt/dextorm
COPY ./target/benchmark.jar /opt/dextorm
CMD ["java","-jar","/opt/dextorm/benchmark.jar"]