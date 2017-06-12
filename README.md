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

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management 

## Authors

* **Guillaume Lours** - *Initial work* - [glours](https://github.com/glours)
* **Jérôme Avoustin** - *Initial work* - [rehia](https://github.com/rehia)

See also the list of [contributors](https://github.com/saagie/whyat-server/contributors) who participated in this project.

## License

This project is licensed under the Apache Licence version 2.0 - see the [LICENSE](LICENSE) file for details
