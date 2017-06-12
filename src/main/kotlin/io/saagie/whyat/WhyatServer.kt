package io.saagie.whyat

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class WhyatServer

fun main(args: Array<String>) {
    SpringApplication.run(WhyatServer::class.java, *args)
}
