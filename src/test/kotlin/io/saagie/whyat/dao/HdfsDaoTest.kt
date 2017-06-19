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

import org.amshove.kluent.shouldEndWith
import org.amshove.kluent.shouldEqualTo
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import java.util.*

internal class HdfsDaoTest : Spek({

    describe("in a context of a hdfsDao") {
        val hdfsDao = HdfsDao()
        on("getFileName") {
            val fileName = hdfsDao.getFilename()
            it("should return a filename ends with a .csv") {
                fileName shouldEndWith ".csv"
            }

            it("should return a filename with 2 \"_\"") {
                (fileName.split("_").size - 1) shouldEqualTo 2
            }

            it("should return a filename depends on the day") {
                val year = fileName.substring(0, fileName.indexOf("_"))
                val month = fileName.substring(fileName.indexOf("_") + 1, fileName.lastIndexOf("_"))
                val day = fileName.substring(fileName.lastIndexOf("_") + 1, fileName.indexOf(".csv"))
                val fileDate = Calendar.getInstance()
                fileDate.set(year.toInt(), month.toInt() - 1, day.toInt())
                val date = Calendar.getInstance()
                date.get(Calendar.YEAR) shouldEqualTo fileDate.get(Calendar.YEAR)
                date.get(Calendar.MONTH) shouldEqualTo fileDate.get(Calendar.MONTH)
                date.get(Calendar.DAY_OF_MONTH) shouldEqualTo fileDate.get(Calendar.DAY_OF_MONTH)
            }
        }

    }
})