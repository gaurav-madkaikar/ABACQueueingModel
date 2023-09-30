#!/bin/bash
if [ ! -e "bin" ];
then
    mkdir bin
fi

cd src/

if [ "${OS}" = "Windows_NT" ]; then
# on Microsoft Windows, use semicolon as a separator in classpath.
javac -cp "../lib/commons-math-2.2.jar;../lib/commons-math3-3.3.jar;." -d ../bin edu/dar/util/*.java
javac -cp "../lib/commons-math-2.2.jar;../lib/commons-math3-3.3.jar;." -d ../bin edu/dar/algo/*.java
else
# on other OSs, use colon as a separator in classpath.
javac -cp "../lib/commons-math-2.2.jar:../lib/commons-math3-3.3.jar:." -d ../bin edu/dar/util/*.java
javac -cp "../lib/commons-math-2.2.jar:../lib/commons-math3-3.3.jar:." -d ../bin edu/dar/algo/*.java
fi;
