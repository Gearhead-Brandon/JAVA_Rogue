#!/bin/bash

clear

gradle clean
gradle jar
java -jar build/libs/Rogue-1.8.10.jar
