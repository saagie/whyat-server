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

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.saagie.whyat.domain.Event
import org.springframework.beans.factory.annotation.Value
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy


class BufferedAvroDao(private val avroDao: AvroDao) : EventDao {

    @Value("\${avro.maxBatchSize}")
    private val maxBatchSize = 1000

    @Value("\${avro.batchIdle}")
    private val batchIdle = 2000L

    private val eventSubject: PublishSubject<Event> = PublishSubject.create()

    private val writeBatchPool = Executors.newSingleThreadExecutor()

    @PostConstruct
    fun init() {
        val batchOnIdleExpiredObservable = batchOnIdleExpired()

        val batchOnMaxSizeOrIdleExpiredObservable = batchOnMaxSizeOrIdleExpired(batchOnIdleExpiredObservable)

        this.eventSubject
                .buffer(batchOnMaxSizeOrIdleExpiredObservable)
                .subscribe { batch -> this.writeBatch(batch) }
    }

    private fun batchOnIdleExpired(): Observable<Boolean> {
        return this.eventSubject
                .debounce(this.batchIdle, TimeUnit.MILLISECONDS)
                .map { sendTick() }
    }

    private fun batchOnMaxSizeOrIdleExpired(batchOnIdleExpiredObservable: Observable<Boolean>): Observable<Boolean>? {
        return Observable.merge(this.eventSubject.map { `continue`() }, batchOnIdleExpiredObservable.map { reset() })
                .scan(0, { count, message ->
                    if (canReset(message, count)) reset() else increment(count)
                })
                .filter { message -> resetSent(message) }
                // send tick on either idle time expired, or batch size reached
                .map { sendTick() }
    }

    private fun canReset(message: Int, count: Int) = endOfIdleTime(message) || batchMaxSizeReached(count)

    private fun sendTick() = true

    private fun resetSent(message: Int) = message == reset()

    private fun batchMaxSizeReached(count: Int) = increment(count) == this.maxBatchSize

    private fun increment(count: Int) = count + 1

    private fun `continue`() = 1

    private fun endOfIdleTime(message: Int) = message == reset()

    private fun reset() = 0

    private fun writeBatch(batch: MutableList<Event>) {
        this.writeBatchPool.submit {
            this.avroDao.storeEvents(batch)
        }
    }

    override fun storeEvent(event: Event) {
        this.eventSubject.onNext(event)
    }

    @PreDestroy
    fun destroy() {
        this.writeBatchPool.shutdownNow()
    }
}