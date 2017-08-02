/**
 * Copyright © 2017 Saagie (contact@saagie.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
