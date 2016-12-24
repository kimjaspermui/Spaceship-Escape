.SUFFIXES: .java .class
.java.class:
	javac -cp ./Acme.jar:./objectdraw.jar:. $<

#SpaceshipEscapeController.class:SpaceshipEscapeController.java
#	javac -cp ./Acme.jar:./objectdraw.jar:. SpaceshipEscapeController.java

run : SpaceshipEscapeController.class
	java -cp ./Acme.jar:./objectdraw.jar:. SpaceshipEscapeController

clean:
	rm -f *.class

new:
	make clean
	make run

cpu:
	javac -J-agentlib:hprof=cpu=times -cp ./Acme.jar:./objectdraw.jar:. SpaceshipEscapeController.java
