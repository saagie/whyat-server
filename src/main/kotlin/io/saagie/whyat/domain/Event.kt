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
package io.saagie.whyat.domain

import java.time.LocalDateTime
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

data class Event(
        val applicationID: String?,
        val platformID: String?,
        val browser: BrowserInformation,
        val user: User,
        var type: String,
        val timestamp: Long,
        val recordDate: LocalDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault()),
        val uri: String,
        var payload: Payload) {

    fun toCSVHeader(): String {
        return "applicationID,platformID,${browser.toCSVHeader()},${user.toCSVHeader()},type,timestamp,recordDate,uri,${payload.toCSVHeader()}\n"
    }

    fun toCSV(): String {
        return "$applicationID,$platformID,${browser.toCSV()},${user.toCSV()},$type,$timestamp,${recordDate.format(DateTimeFormatter.ISO_DATE_TIME)},${escape(uri)},${payload.toCSV()}\n"
    }

    fun escape(original: String): String {
        return original.replace(',', ';')
    }
}