package io.saagie.whyat.dao

import io.saagie.whyat.domain.Example
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.Path
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository
import java.net.URI


@Repository
class HdfsDao {

    @Value("\${hdfs.ip}")
    var hdfsIp = ""

    @Value("\${hdfs.port}")
    var hdfsPort = 0

    @Value("\${hdfs.path}")
    var hdfsPath = ""

    var fs: FileSystem? = null

    fun init() {
        if (fs == null) {
            val hdfsUri = "hdfs://$hdfsIp:$hdfsPort"
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


    fun appendExample(example: Example) {
        this.init()
        val fileName = "test.csv"
        val hdfswritepath = Path(hdfsPath + "/" + fileName)
        //Init output stream
        if (!fs!!.exists(hdfswritepath)) {
            val outputStream = fs!!.create(hdfswritepath)
            outputStream.writeBytes("id;value\n")
            outputStream.close()
        }
        val outputStream = fs!!.append(hdfswritepath)
        //Cassical output stream usage
        outputStream.writeBytes("${example.id};${example.value}\n")
        outputStream.close()
    }
}