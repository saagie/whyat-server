package io.saagie.whyat.domain

data class Example(
        val id: Int,
        val value: String) {

    fun toCSV(): String {
        return "$id;$value\n"
    }

    fun toCSVHeader(): String {
        return "id;value\n"
    }
}