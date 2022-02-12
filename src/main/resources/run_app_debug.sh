#!/bin/bash
java -agentlib:jdwp=transport=dt_socket,server=y,address=0.0.0.0:5005,suspend=y -javaagent:"dextorm.jar" -jar basic-uni-cli.jar
