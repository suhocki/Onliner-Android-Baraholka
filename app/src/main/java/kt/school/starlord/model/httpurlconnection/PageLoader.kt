package kt.school.starlord.model.httpurlconnection

import kt.school.starlord.BuildConfig
import java.net.HttpURLConnection
import java.net.URL

class PageLoader {
    private val url: URL = URL(BuildConfig.BARAHOLKA_ONLINER_URL)

    fun getPage(): String {
        val urlConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
        return urlConnection.inputStream.bufferedReader().use { it.readText() }
    }
}