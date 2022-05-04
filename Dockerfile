FROM gradle:7.2.0-jdk11
VOLUME /tmp
WORKDIR /app
COPY . .
RUN gradle clean build -x test
CMD gradle bootRun --args='--spring.profiles.active=dev'
