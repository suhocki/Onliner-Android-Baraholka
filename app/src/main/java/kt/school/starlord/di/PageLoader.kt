package kt.school.starlord.di

import java.net.HttpURLConnection
import java.net.URL

class PageLoader {
    private val url: URL = URL("https://baraholka.onliner.by")
    private val urlConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
    val page = try {
        urlConnection.inputStream.bufferedReader().readText()
    } finally {
        urlConnection.disconnect()
    }
}