#!/bin/bash
#cd target
java -Xmx1024m -cp  target/stream-simulator-1.0-SNAPSHOT-jar-with-dependencies.jar com.hortonworks.App "$@" 
