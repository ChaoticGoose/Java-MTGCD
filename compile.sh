#!/bin/bash

javac java/*.java -d output
jar cfm MTGCD.jar output/Manifest  -C output/ .
