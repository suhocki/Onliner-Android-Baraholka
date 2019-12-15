package kt.school.starlord.model.repository.network

import android.net.Uri
import kotlinx.coroutines.withContext
import kt.school.starlord.BuildConfig
import kt.school.starlord.domain.repository.category.CategoriesNetworkRepository
import kt.school.starlord.domain.repository.product.ProductsNetworkRepository
import kt.school.starlord.domain.system.coroutine.CoroutineContextProvider
import java.net.URL

/**
 * Fetch data through the Internet connection.
 */
class NetworkRepository(contexts: CoroutineContextProvider) : CategoriesNetworkRepository, ProductsNetworkRepository {

    private val io = contexts.io

    override suspend fun downloadCategoriesPage() = withContext(io) {
        URL(BuildConfig.BARAHOLKA_ONLINER_URL)
            .openStream()
            .bufferedReader()
            .readText()
    }

    override suspend fun downloadProductsPage(forumId: Long) = withContext(io) {
        val uri = Uri.parse(BuildConfig.BARAHOLKA_ONLINER_URL)
            .buildUpon()
            .appendPath(FORUM_PAGE)
            .appendQueryParameter(FORUM_ID, forumId.toString())
            .build()

        URL(uri.toString())
            .openStream()
            .bufferedReader()
            .readText()
    }

    companion object {
        private const val FORUM_PAGE = "viewforum.php"
        private const val FORUM_ID = "f"
    }
}
