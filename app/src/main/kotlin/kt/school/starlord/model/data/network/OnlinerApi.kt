package kt.school.starlord.model.data.network

import java.net.URL
import kt.school.starlord.BuildConfig

/**
 * Implements network requests using URLConnection.
 */
class OnlinerApi {

    /**
     * Loads HTML page from url.
     */
    fun loadPage(): String {
        val connection = URL(BuildConfig.BARAHOLKA_ONLINER_URL).openConnection()
        return connection.inputStream.bufferedReader().use { it.readText() }
    }
}
