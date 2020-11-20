#! /bin/bash
rm project3.jar
javac -cp lib/coremidi4j-1.1.jar -d build src/*.java
jar cfm project3.jar src/manifest.txt -C build .
java -jar project3.jar 0 1