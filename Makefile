

all: Tkl2Gpx.class

Tkl2Gpx.class: Tkl2Gpx.java
	javac Tkl2Gpx.java

clean: 
	rm Tkl2Gpx.class
