#! /bin/bash
rm -f magicVoice.jar
javac src/*.java -d build
jar cfm magicVoice.jar src/manifest.txt -C build .
java -jar magicVoice.jar