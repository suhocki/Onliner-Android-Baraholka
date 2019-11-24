package kt.school.starlord.model.data.jsoup

import kotlinx.coroutines.withContext
import kt.school.starlord.BuildConfig
import kt.school.starlord.domain.system.coroutine.CoroutineContextProvider
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.net.URL

class JsoupDataSource(private val coroutineContextProvider: CoroutineContextProvider) {

    suspend fun getCategoriesElements(): Map<Element, Elements> = withContext(coroutineContextProvider.io) {
        Jsoup.parse(URL(BuildConfig.BARAHOLKA_ONLINER_URL), BuildConfig.NETWORK_REQUEST_TIMEOUT_MILLIS)
            .getElementsByClass(CATEGORIES)
            .associateWith {
                it.getElementsByClass(SUBCATEGORIES)
                    .first()
                    .select(SUBCATEGORIES_DATA)
            }
    }

    suspend fun getProductElements(url: URL): Elements = withContext(coroutineContextProvider.io) {
        Jsoup.parse(url, BuildConfig.NETWORK_REQUEST_TIMEOUT_MILLIS)
            .getElementsByClass(PRODUCT_CLASS)
            .flatMap { it.getElementsByTag(PRODUCT_TAG) }
            .filter {
                it.getElementsByClass(PRODUCT_TITLE).hasText() &&
                        it.getElementsByClass(PRODUCT_SIGNATURE).first() != null &&
                        it.getElementsByClass(PRODUCT_COST).first() != null
            }.toElements()
    }

    private fun List<Element>.toElements(): Elements {
        return Elements(this)
    }

    companion object {
        private const val CATEGORIES = "cm-onecat"
        private const val PRODUCT_CLASS = "ba-tbl-list__table"
        private const val PRODUCT_TAG = "tr"
        private const val PRODUCT_TITLE = "wraptxt"
        private const val PRODUCT_SIGNATURE = "ba-signature"
        private const val PRODUCT_COST = "cost"
        private const val SUBCATEGORIES = "b-cm-list"
        private const val SUBCATEGORIES_DATA = "li"
    }
}