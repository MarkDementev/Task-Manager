setup:
	gradle wrapper --gradle-version 7.4.2

clean:
	./gradlew clean

build:
	./gradlew clean build

install:
	./gradlew installDist

lint:
	./gradlew checkstyleMain checkstyleTest

test:
	./gradlew test

report:
	./gradlew jacocoTestReport

check-updates:
	./gradlew dependencyUpdates


.PHONY: build
