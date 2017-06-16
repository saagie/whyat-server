package io.saagie.whyat.service

import io.saagie.whyat.dao.HdfsDao
import io.saagie.whyat.domain.Event
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class EventService {

    @Autowired
    lateinit var hdfsDao: HdfsDao

    fun recordEvent(event: Event): Event {
        hdfsDao.storeEvent(event)
        return event
    }
}