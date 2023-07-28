### CI status:
[![Actions Status](https://github.com/MarkDementev/java-project-73/workflows/Java%20CI/badge.svg)](https://github.com/MarkDementev/java-project-73/actions)
[![Maintainability](https://api.codeclimate.com/v1/badges/daa04eb4089048d5b7ee/maintainability)](https://codeclimate.com/github/MarkDementev/java-project-73/maintainability)
[![Test Coverage](https://api.codeclimate.com/v1/badges/daa04eb4089048d5b7ee/test_coverage)](https://codeclimate.com/github/MarkDementev/java-project-73/test_coverage)

### Hexlet tests and linter status:
[![Actions Status](https://github.com/MarkDementev/java-project-73/workflows/hexlet-check/badge.svg)](https://github.com/MarkDementev/java-project-73/actions)

# Overview

This project is a task management system. It allows you to set tasks, assign performers and change their statuses. Registration and authentication are required to work with the system.

## System requirements

* Java >= 20.0.1
* Gradle 8.2.1

## Setup

```sh
make install # ./gradlew installDist
make start # run server http://localhost:5001
make test # run tests
make lint # run linter

make generate-migrations # new migrations if added models corrections
```

# Project link

https://markdementev-java-project-73.onrender.com

# Documentation link

https://markdementev-java-project-73.onrender.com/swagger.html
