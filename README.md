[![Build Actions Status](https://github.com/JanSimek/parcel-homework/workflows/Build/badge.svg)](https://github.com/{userName}/{repoName}/actions) [![codebeat badge](https://codebeat.co/badges/2973ed5a-e2bb-4e2f-9068-24e3b94b1088)](https://codebeat.co/projects/github-com-jansimek-parcel-homework-master) [![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=JanSimek_parcel-homework&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=JanSimek_parcel-homework) 

# Parcel-parsing homework

Calculates and prints total weights of packages and (optionally) their shipping fees. 

## Usage

```
Usage: PostOffice [-hV] [-f=fees.txt] -p=parcels.txt
  -p, --parcels=parcels.txt
                        File containing package list
  -f, --fees=fees.txt   File containing shipping fees
  -h, --help            Show this help message and exit.
  -V, --version         Print version information and exit.
```

### Compile and run

To compile and execute the program open the terminal in the project directory and type:

```shell
mvn clean compile exec:java -Dexec.args="-p=src/test/resources/parcels.txt -f=src/test/resources/fees.txt"
```

The program will calculate total weights of packages and their shipping fees for each postal code. For example:

```
08801 15.960 5.00
08079 5.500 2.50
09300 3.200 2.00
90005 2.000 1.50
```

You can then add a new package by typing in weight and postal code, e.g. 6.8 09300

Invalid input of user data will result in an explanatory error message printed in the console. For example -3 99999 will result in:

```
ERROR: Weight cannot be negative
```

Updated list will be reprinted to the console periodically every minute.

### Tests

To run tests using Surefire:

```shell
mvn clean test
```

## System requirements

This software was developed on Arch linux and tested on Windows using OpenJDK 11.
