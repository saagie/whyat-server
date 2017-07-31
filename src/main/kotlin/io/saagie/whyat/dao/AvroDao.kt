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
package io.saagie.whyat.dao

import io.saagie.whyat.domain.Event
import org.apache.avro.Schema
import org.apache.avro.file.CodecFactory
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.Path
import java.net.URI
import java.time.format.DateTimeFormatter
import javax.annotation.PostConstruct
import org.apache.avro.generic.GenericRecord
import org.apache.avro.file.DataFileWriter
import org.apache.avro.generic.GenericDatumWriter
import org.apache.avro.generic.GenericData
import org.apache.avro.mapred.FsInput
import org.apache.hadoop.fs.FSDataOutputStream
import java.io.File


class AvroDao(
        private val hdfsHost: String,
        private val hdfsPath: String) : EventDao {
    var fs: FileSystem? = null

    var configuration = Configuration()

    var schema: Schema? = null

    @PostConstruct
    fun init() {
        if (fs == null) {
            val hdfsUri = "hdfs://$hdfsHost"
            configuration = configuration.apply {
                // Set FileSystem URI
                set("fs.defaultFS", hdfsUri)
                // Because of Maven
                set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem::class.java.name)
                set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem::class.java.name)
            }
            // Set HADOOP user
            System.setProperty("HADOOP_USER_NAME", "hdfs")
            System.setProperty("hadoop.home.dir", "/")

            fs = FileSystem.get(URI.create(hdfsUri), configuration)

            createSchema()
            schema = loadSchema()
        }
    }

    @Synchronized
    override fun storeEvent(event: Event) {
        prepareDataFileWriter().use { writer -> writer.append(avroRecord(event, schema!!))}
    }

    @Synchronized
    fun storeEvents(events: List<Event>) {
        prepareDataFileWriter().use {writer ->
            events.forEach { event ->
                writer.append(avroRecord(event, schema!!))
            }
        }
    }

    private fun prepareDataFileWriter(): DataFileWriter<GenericRecord> {
        val dataWriter = GenericDatumWriter<GenericRecord>(schema)
        val dataFileWriter = DataFileWriter(dataWriter)
                .setCodec(CodecFactory.snappyCodec())

        val dataFilePath = this.getDataFilePath()
        val mustAppendToFile = fs!!.exists(getDataFilePath())
        val dataFileOutputStream = openDataFile(dataFilePath)

        if (mustAppendToFile) {
            return dataFileWriter.appendTo(FsInput(dataFilePath, configuration), dataFileOutputStream)
        }
        return dataFileWriter.create(schema, dataFileOutputStream)
    }

    private fun createSchema() {
        val schemaFilePath = getSchemaFilePath()
        val schemaTemplatePath = Path(schemaTemplatePath())
        fs!!.copyFromLocalFile(schemaTemplatePath, schemaFilePath)
    }

    private fun schemaTemplatePath() = this.javaClass.classLoader.getResource("whyat.avsc").path

    private fun getSchemaFilePath(): Path {
        return Path(hdfsPath + "/whyat.avsc")
    }

    private fun loadSchema(): Schema {
        return Schema.Parser().parse(File(schemaTemplatePath()))
    }

    @Synchronized
    private fun openDataFile(dataFilePath: Path): FSDataOutputStream {
        if (fs!!.exists(dataFilePath)) {
            return fs!!.append(dataFilePath)
        }
        return fs!!.create(dataFilePath)
    }

    fun getDataFilePath(): Path {
        return Path(hdfsPath + "/data/whyat.avro")
    }

    fun avroRecord(event: Event, schema: Schema): GenericRecord {
        val record = GenericData.Record(schema)
        record.put("applicationID", event.applicationID)
        record.put("platformID", event.platformID)
        record.put("type", event.type)
        record.put("timestamp", event.timestamp)
        record.put("recordDate", event.recordDate.format(DateTimeFormatter.ISO_DATE_TIME))
        record.put("uri", event.uri)
        record.put("userID", event.user.id)
        record.put("appCodeName", event.browser.appCodeName)
        record.put("appName", event.browser.appName)
        record.put("version", event.browser.appVersion)
        record.put("platform", event.browser.platform)
        record.put("userAgent", event.browser.userAgent)
        record.put("payload", event.payload.toJSON())

        return record
    }
}