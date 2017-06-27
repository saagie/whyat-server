FROM java:8-jre
MAINTAINER Saagie <contact@saagie.com>

ENTRYPOINT ["/usr/bin/java", "-jar", "/usr/share/whyat/whyat-server-0.1.0.jar"]

EXPOSE 8080

# Add the service itself
ADD target/whyat-server-0.1.0.jar /usr/share/whyat/whyat-server-0.1.0.jar
