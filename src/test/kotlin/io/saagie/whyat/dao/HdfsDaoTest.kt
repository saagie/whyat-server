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

import org.amshove.kluent.shouldMatch
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

internal class HdfsDaoTest : Spek({

    describe("in a context of a eventDao") {
        val hdfsDao = HdfsDao("localhost:8020", "/test/hdfs")
        on("getFileName") {
            val fileName = hdfsDao.getFilename()
            it("should return a filename with format yyyy_MM_dd.csvh") {
                fileName shouldMatch Regex("\\d{4}_\\d{2}_\\d{2}.csvh")
            }
        }
    }
})