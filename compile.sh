#! /bin/bash
rm project1-2.jar
javac -cp lib/coremidi4j-1.1.jar -d build src/*.java
jar cfm project1-2.jar src/manifest.txt -C build .
java -jar project1-2.jar 0 1