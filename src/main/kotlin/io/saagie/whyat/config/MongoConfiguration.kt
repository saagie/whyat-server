package io.saagie.whyat.config

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.mongodb.MongoClient
import org.jongo.Jongo
import org.jongo.marshall.jackson.JacksonMapper
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration
import org.springframework.boot.autoconfigure.mongo.MongoProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(value = MongoAutoConfiguration::class)
class MongoConfiguration {

    @Bean
    fun jongo(mongo: MongoClient, properties: MongoProperties): Jongo {
        val database = mongo.getDB(properties.database)
        val mapper = JacksonMapper.Builder()
                .registerModule(JavaTimeModule())
                .build()
        return Jongo(database, mapper)
    }
}
