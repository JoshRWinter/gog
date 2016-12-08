#!/bin/bash

javac *.java

if [ $? -eq 1 ]; then
	exit
fi

jar cfm gog.jar manifest *.class # jar it up

