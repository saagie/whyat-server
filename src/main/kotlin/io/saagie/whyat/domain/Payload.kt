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

import com.fasterxml.jackson.annotation.JsonAnyGetter
import com.fasterxml.jackson.annotation.JsonAnySetter
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.ObjectMapper
import java.util.HashMap

class Payload {
    @JsonIgnore
    private val properties = HashMap<String, Any>()

    @JsonAnyGetter
    fun getProperties(): Map<String, Any> {
        return this.properties
    }

    @JsonAnySetter
    fun setProperty(name: String, value: Any) {
        this.properties.put(name, value)
    }

    fun toCSVHeader(): String {
        return this.properties.keys.joinToString(",")
    }

    fun toCSV(): String {
        return this.properties.values.map { value -> escape(value.toString()) }.joinToString(",")
    }

    fun escape(original: String): String {
        return original.replace(',', ';')
    }

    fun toJSON() : String {
        return ObjectMapper().writeValueAsString(this.properties)
    }
}