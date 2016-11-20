.PHONY: all
all: gog.jar
	java -jar gog.jar

gog.jar: Main.class GamePanel.class Node.class NodeWrapper.class
	jar cfm gog.jar manifest *.class

Main.class: Main.java
	javac Main.java

GamePanel.class: GamePanel.java
	javac GamePanel.java

Node.class: Node.java
	javac Node.java

NodeWrapper.class: NodeWrapper.java
	javac NodeWrapper.java

.PHONY: clean
clean:
	rm *.class
