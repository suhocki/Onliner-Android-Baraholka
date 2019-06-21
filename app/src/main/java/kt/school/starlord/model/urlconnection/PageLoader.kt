package kt.school.starlord.model.urlconnection

import java.net.URL

/**
 * Implements network requests using URLConnection.
 */
class PageLoader(private val url: URL) {

    /**
     * Loads HTML page from url.
     */
    fun getPage(): String {
        val connection = url.openConnection()
        return connection.inputStream.bufferedReader().use { it.readText() }
    }
}
