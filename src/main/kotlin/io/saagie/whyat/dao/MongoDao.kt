package io.saagie.whyat.dao

import io.saagie.whyat.domain.Event
import org.jongo.*

class MongoDao(private val jongo: Jongo) : EventDao {

    val whyatCollection: MongoCollection = jongo.getCollection("events")

    override fun storeEvent(event: Event) {
        whyatCollection.insert(event)
    }
}