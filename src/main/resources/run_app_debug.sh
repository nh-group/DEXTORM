#!/bin/bash

java \
    -agentlib:jdwp=transport=dt_socket,server=y,address=0.0.0.0:5005,suspend=y\
    -javaagent:"agents/org.jacoco.agent-0.8.7-runtime.jar=dumponexit=true,output=tcpserver"\
    -javaagent:"dextorm.jar=configurationFile->dextorm.yaml" \
    -jar basic-uni-cli.jar


