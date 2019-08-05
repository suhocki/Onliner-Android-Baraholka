package kt.school.starlord.model.repository.network

import java.net.URL
import kotlinx.coroutines.withContext
import kt.school.starlord.BuildConfig
import kt.school.starlord.domain.repository.CategoriesWithSubcategoriesRepository
import kt.school.starlord.domain.repository.ProductsListRepository
import kt.school.starlord.domain.system.coroutine.CoroutineContextProvider
import kt.school.starlord.entity.category.CategoriesWithSubcategories
import kt.school.starlord.entity.product.Product
import kt.school.starlord.entity.product.ProductsList
import kt.school.starlord.model.data.mapper.Mapper
import org.jsoup.Jsoup

/**
 * Implement methods using Internet connection.
 */
class NetworkRepository(
    private val mapper: Mapper,
    private val coroutineContextProvider: CoroutineContextProvider
) : CategoriesWithSubcategoriesRepository, ProductsListRepository {

    override suspend fun getCategoriesWithSubcategories(): CategoriesWithSubcategories =
        withContext(coroutineContextProvider.io) {
            val jsoupDocument = Jsoup.parse(
                URL(BuildConfig.BARAHOLKA_ONLINER_URL),
                BuildConfig.NETWORK_REQUEST_TIMEOUT_MILLIS
            )
            @Suppress("RemoveExplicitTypeArguments")
            mapper.map<CategoriesWithSubcategories>(jsoupDocument)
        }

    override suspend fun getProducts(link: String): List<Product> =
        withContext(coroutineContextProvider.io) {
            val jsoupDocument = Jsoup.parse(
                mapper.map(link),
                BuildConfig.NETWORK_REQUEST_TIMEOUT_MILLIS
            )
            mapper.map<ProductsList>(jsoupDocument).products
        }
}
