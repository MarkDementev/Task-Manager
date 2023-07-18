FROM eclipse-temurin:20-jdk

WORKDIR ./

COPY ./ .

RUN ./gradlew installDist

CMD ./build/install/app/bin/app
