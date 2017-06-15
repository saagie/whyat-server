FROM java:8-jre
MAINTAINER Saagie <contact@saagie.com>

ENTRYPOINT ["/usr/bin/java", "-jar", "/usr/share/whyat/whyat-server-0.0.1.jar"]

# Add the service itself
ADD target/whyat-server-0.0.1.jar /usr/share/whyat/whyat-server-0.0.1.jar