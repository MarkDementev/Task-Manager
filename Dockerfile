FROM eclipse-temurin:20-jdk

WORKDIR ./

COPY ./ .

RUN gradle installDist

CMD ./build/install/app/bin/app
