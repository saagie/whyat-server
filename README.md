[![Build Status](https://travis-ci.org/saagie/whyat-server.svg?branch=master)](https://travis-ci.org/saagie/whyat-server)
# Y@ Server

Y@ (whyat) helps you to track your user’s action within your application and store it directly in the data source of your choice

## Getting Started

Fork, then clone the repo: 
```
git clone git@github.com:your-github-handle/whyat-server.git
```

Make sure tests pass : 
```
mvnw clean test
```
or 
```
mvn clean test
```

And check the project build and package correctly on your development environnement : 
```
mvnw clean package
```
or
```
mvn clean package
```

### Prerequisites

Y@ Server is a Spring Boot Application using Kotlin, you can install a plugin for your favorite IDE

* IntelliJ Idea manage natively Kotlin Project since version 15 (even in community edition)
* Install the [Kotlin plugin for Eclipse](https://kotlinlang.org/docs/tutorials/getting-started-eclipse.html)

If you want to use the Kotlin CLI, you can follow [this tutorial](https://kotlinlang.org/docs/tutorials/command-line.html)


## Running the tests

```
mvn clean test
```

## Docker images

To generate docker images (two tags are generated : latest and project.version) : 

```
mvn docker:build
```

## Environment variables

Before start the application or the docker image, you need to setup 3 env. variables : 

* WHYAT_HDFS_IP : the IP or DNS-Name of the Namenode.
* WHYAT_HDFS_PORT : The port of the Namenode (default is 8020).
* WHYAT_PATH : The path you want to store CSV file in your datalake (if not exist, the application will create it. The directory or the parent should be writable by user "hdfs")
 
## Built With

* [Maven](https://maven.apache.org/) - Dependency Management 

## Authors

* **Guillaume Lours** - *Initial work* - [glours](https://github.com/glours)
* **Jérôme Avoustin** - *Initial work* - [rehia](https://github.com/rehia)
* **Pierre Leresteux** - *Kotlin/docker part* - [pierreLeresteux](https://github.com/pierreLeresteux)

See also the list of [contributors](https://github.com/saagie/whyat-server/contributors) who participated in this project.

## License

This project is licensed under the Apache Licence version 2.0 - see the [LICENSE](LICENSE) file for details

#### Maven plugin

When you create a new file (Kotlin/Java) in this project, don't forget to add the Licence header, or simply run

```
mvn licence:format
```

It will add the header for all source files.