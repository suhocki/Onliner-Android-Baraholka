package kt.school.starlord.model.repository.network

import kotlinx.coroutines.withContext
import kt.school.starlord.BuildConfig
import kt.school.starlord.domain.entity.category.CategoriesWithSubcategories
import kt.school.starlord.domain.entity.product.Product
import kt.school.starlord.domain.repository.CategoriesWithSubcategoriesRepository
import kt.school.starlord.domain.repository.product.ProductsRepository
import kt.school.starlord.domain.system.coroutine.CoroutineContextProvider
import kt.school.starlord.domain.data.mapper.Mapper
import org.jsoup.Jsoup
import java.net.URL

/**
 * Fetch data through the Internet connection.
 */
class NetworkRepository(
    private val mapper: Mapper,
    private val coroutineContextProvider: CoroutineContextProvider
) : CategoriesWithSubcategoriesRepository, ProductsRepository {

    override suspend fun getCategoriesWithSubcategories(): CategoriesWithSubcategories =
        withContext(coroutineContextProvider.io) {
            val jsoupDocument = Jsoup.parse(
                URL(BuildConfig.BARAHOLKA_ONLINER_URL),
                BuildConfig.NETWORK_REQUEST_TIMEOUT_MILLIS
            )
            @Suppress("RemoveExplicitTypeArguments")
            mapper.map<CategoriesWithSubcategories>(jsoupDocument)
        }

    override suspend fun getProducts(link: String) = withContext(coroutineContextProvider.io) {
        val productsUrl = mapper.map<URL>(link)

        Jsoup.parse(productsUrl, BuildConfig.NETWORK_REQUEST_TIMEOUT_MILLIS)
            .getElementsByClass(PRODUCT_CLASS)
            .flatMap { it.getElementsByTag(PRODUCT_TAG) }
            .filter {
                it.getElementsByClass(PRODUCT_TITLE).hasText() &&
                        it.getElementsByClass(PRODUCT_SIGNATURE).first() != null &&
                        it.getElementsByClass(PRODUCT_COST).first() != null
            }
            .map { mapper.map<Product>(it) }
    }

    companion object {
        private const val PRODUCT_CLASS = "ba-tbl-list__table"
        private const val PRODUCT_TAG = "tr"
        const val PRODUCT_TITLE = "wraptxt"
        const val PRODUCT_SIGNATURE = "ba-signature"
        const val PRODUCT_COST = "cost"
    }
}
