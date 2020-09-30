#! /bin/bash
rm project2.jar
javac -cp lib/coremidi4j-1.1.jar -d build src/*.java
jar cfm project2.jar src/manifest.txt -C build .
java -jar project2.jar 0 1