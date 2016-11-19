.PHONY: all
all: gog.jar
	java -jar gog.jar

gog.jar: Main.class
	jar cfm gog.jar manifest *.class

Main.class: Main.java
	javac Main.java
