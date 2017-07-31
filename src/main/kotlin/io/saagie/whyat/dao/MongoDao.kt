package io.saagie.whyat.dao

import io.saagie.whyat.domain.Event
import org.jongo.*
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Repository

@Repository
@Primary
class MongoDao(private val jongo: Jongo) : EventDao {

    val whyatCollection: MongoCollection = jongo.getCollection("events")

    override fun storeEvent(event: Event) {
        whyatCollection.insert(event)
    }
}