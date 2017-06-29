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

data class BrowserInformation(
        val appName: String,
        val appCodeName: String,
        val appVersion: String,
        val platform: String,
        val userAgent: String) {

    fun toCSVHeader(): String {
        return "appName,appCodeName,version,platform,user-agent"
    }

    fun toCSV(): String {
        return "$appName,${escape(appCodeName)},${escape(appVersion)},${escape(platform)},${escape(userAgent)}"
    }

    fun escape(original: String): String {
        return original.replace(',', ';')
    }
}