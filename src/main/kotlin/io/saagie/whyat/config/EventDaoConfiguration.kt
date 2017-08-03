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
import com.mongodb.MongoCredential
import com.mongodb.ServerAddress
import io.saagie.whyat.dao.*
import org.jongo.Jongo
import org.jongo.marshall.jackson.JacksonMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.naming.ConfigurationException
import org.springframework.context.ApplicationContext
import org.springframework.core.env.Environment


@Configuration
class EventDaoConfiguration(
        private val context: ApplicationContext,
        private val environment: Environment) {

    @Value("\${whyat.event.dao}")
    var daoName = "mongo"

    @Bean
    fun eventDao(): EventDao {
        return when (daoName) {
            "", "mongo", "mongodb" -> mongoDao()
            "hdfs.avro", "avro" -> avroDao()
            "hdfs.csv" -> return csvDao()
            else -> {
                throw ConfigurationException("Unknown dao implementation. Choose between mongo, hdfs.avro, hdfs.csv")
            }
        }
    }

    private fun csvDao(): HdfsDao {
        val csvDao = HdfsDao(environment.getProperty("hdfs.url"), environment.getProperty("hdfs.path"))
        csvDao.init()
        return csvDao
    }

    private fun avroDao(): BufferedAvroDao {
        val avroDao = AvroDao(environment.getProperty("hdfs.url"), environment.getProperty("hdfs.path"))
        avroDao.init()
        val bufferedDao = BufferedAvroDao(avroDao)
        bufferedDao.init()
        return bufferedDao
    }

    private fun mongoDao(): MongoDao {
        val properties = MongoProperties(this.environment)
        return MongoDao(jongo(mongoClient(properties), properties.database()))
    }

    private fun jongo(mongo: MongoClient, databaseName: String): Jongo {
        val database = mongo.getDB(databaseName)
        val mapper = JacksonMapper.Builder()
                .registerModule(JavaTimeModule())
                .build()
        return Jongo(database, mapper)
    }

    private fun mongoClient(properties: MongoProperties) : MongoClient =
            MongoClient(properties.serverAddress(), properties.credentials())

    private class MongoProperties(environment: Environment) {
        var prefix = "mongodb."
        var host = environment.getProperty(prefix + "host")
        var port = environment.getProperty(prefix + "port").toInt()
        var username = environment.getProperty(prefix + "username")
        var password = environment.getProperty(prefix + "password")
        var database = environment.getProperty(prefix + "database")

        fun serverAddress() : ServerAddress = ServerAddress(host, port)

        fun credentials() : List<MongoCredential> =
                listOf(MongoCredential.createCredential(this.username, this.database, this.password.toCharArray()))

        fun database() : String = this.database
    }
}
