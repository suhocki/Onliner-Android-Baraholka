package kt.school.starlord.model

import java.net.HttpURLConnection
import java.net.URL

class PageLoader {
    private val url: URL = URL("https://baraholka.onliner.by")

    fun getPage(): String {
        val urlConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
        return urlConnection.inputStream.bufferedReader().use { it.readText() }
    }
}