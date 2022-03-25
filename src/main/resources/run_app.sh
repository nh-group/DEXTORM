#!/bin/bash

java \
    -javaagent:"agents/org.jacoco.agent-${jacoco.version}-runtime.jar=dumponexit=true,output=tcpserver"\
    -javaagent:"dextorm.jar=configurationFile->dextorm.yaml" \
    -jar basic-uni-cli.jar

