FROM gcr.io/google_appengine/openjdk8
VOLUME /tmp
ADD mygae.jar app.jar
CMD [ "java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
