package kt.school.starlord.model

import java.net.HttpURLConnection
import java.net.URL

class PageLoader() {
    private val url: URL = URL("https://baraholka.onliner.by")
    private val urlConnection: HttpURLConnection = url.openConnection() as HttpURLConnection

    fun getPage(): String {
        return try {
            val page = urlConnection.inputStream.bufferedReader().readText()
            page
        } finally {
            urlConnection.disconnect()
        }
    }
}