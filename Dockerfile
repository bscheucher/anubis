# we could use a smaller base-image
FROM openjdk:17.0.2-oraclelinux8

WORKDIR /app

COPY ./build/libs/*.jar app.jar

EXPOSE 80

ENTRYPOINT ["java","-jar","/app/app.jar"]