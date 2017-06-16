package io.saagie.whyat.domain

data class Event(
        val date: String,
        val type: String,
        val user: String,
        val value: String) {

    fun toCSV(): String {
        return "$date;$type;$user;$value\n"
    }

    fun toCSVHeader(): String {
        return "date;type;user;value\n"
    }
}