package io.saagie.whyat.config

import io.saagie.whyat.dao.*
import org.jongo.Jongo
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

    private fun mongoDao() = MongoDao(context.getBean(Jongo::class.java))
}
