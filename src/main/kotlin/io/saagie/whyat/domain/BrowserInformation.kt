package io.saagie.whyat.domain

data class BrowserInformation(
        val name: String,
        val codename: String,
        val version: String,
        val platform: String,
        val userAgent: String) {

    fun toCSVHeader(): String {
        return "name;codename;version;platform;user-agent"
    }

    fun toCSV(): String {
        return "$name;$codename;$version;$platform;$userAgent"
    }


}