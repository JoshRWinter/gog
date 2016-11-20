#!/bin/bash

javac *.java # javac should compile all other files automatically

if [ $? -eq 1 ]; then
	exit
fi

jar cfm gog.jar manifest *.class # jar it up

java -jar gog.jar # run it
