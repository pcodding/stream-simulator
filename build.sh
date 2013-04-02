#!/bin/bash
mvn clean package
zip -r stream-simulator.zip run.sh target
