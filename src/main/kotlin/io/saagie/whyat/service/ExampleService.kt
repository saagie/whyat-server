package io.saagie.whyat.service

import io.saagie.whyat.dao.HdfsDao
import io.saagie.whyat.domain.Example
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ExampleService {

    @Autowired
    lateinit var hdfsDao: HdfsDao

    fun appendExample(example: Example): Example {
        hdfsDao.appendExample(example)
        return example
    }
}