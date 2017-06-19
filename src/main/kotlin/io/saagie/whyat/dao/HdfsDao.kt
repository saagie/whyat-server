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
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.Path
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository
import java.net.URI
import java.util.*
import javax.annotation.PostConstruct


@Repository
class HdfsDao {

    @Value("\${hdfs.url}")
    var hdfsUrl = ""

    @Value("\${hdfs.path}")
    var hdfsPath = ""

    var fs: FileSystem? = null

    @PostConstruct
    fun init() {
        if (fs == null) {
            val hdfsUri = "hdfs://$hdfsUrl"
            val conf = Configuration().apply {
                // Set FileSystem URI
                set("fs.defaultFS", hdfsUri)
                // Because of Maven
                set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem::class.java.name)
                set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem::class.java.name)
            }
            // Set HADOOP user
            System.setProperty("HADOOP_USER_NAME", "hdfs")
            System.setProperty("hadoop.home.dir", "/")

            fs = FileSystem.get(URI.create(hdfsUri), conf)

            val newFolderPath = Path(hdfsPath)
            if (!fs!!.exists(newFolderPath)) {
                // Create new Directory
                fs!!.mkdirs(newFolderPath)
            }
        }
    }


    fun storeEvent(event: Event) {
        val fileName = this.getFilename()
        val hdfswritepath = Path(hdfsPath + "/" + fileName)
        if (!fs!!.exists(hdfswritepath)) {
            val outputStream = fs!!.create(hdfswritepath)
            outputStream.writeBytes(event.toCSVHeader())
            outputStream.close()
        }
        val outputStream = fs!!.append(hdfswritepath)
        outputStream.writeBytes(event.toCSV())
        outputStream.close()
    }

    fun getFilename(): String {
        val cal = Calendar.getInstance()
        return "${cal.get(Calendar.YEAR)}_${cal.get(Calendar.MONTH) + 1}_${cal.get(Calendar.DAY_OF_MONTH)}.csv"
    }
}