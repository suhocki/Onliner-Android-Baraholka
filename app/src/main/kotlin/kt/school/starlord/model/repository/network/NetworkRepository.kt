package kt.school.starlord.model.repository.network

import java.net.URL
import kotlinx.coroutines.withContext
import kt.school.starlord.BuildConfig
import kt.school.starlord.domain.repository.CategoriesWithSubcategoriesRepository
import kt.school.starlord.domain.repository.ProductsListRepository
import kt.school.starlord.domain.system.coroutine.CoroutineContextProvider
import kt.school.starlord.entity.CategoriesWithSubcategories
import kt.school.starlord.entity.product.ProductsList
import kt.school.starlord.model.data.mapper.Mapper
import kt.school.starlord.utils.urlCorrector
import org.jsoup.Jsoup

/**
 * Implement methods using Internet connection.
 */
@Suppress("RemoveExplicitTypeArguments")
class NetworkRepository(
    private val mapper: Mapper,
    private val coroutineContextProvider: CoroutineContextProvider
) : CategoriesWithSubcategoriesRepository, ProductsListRepository {

    override suspend fun getCategoriesWithSubcategories(): CategoriesWithSubcategories =
        get(BuildConfig.BARAHOLKA_ONLINER_URL)

    override suspend fun getProducts(link: String): ProductsList = get(urlCorrector(link))

    private suspend inline fun <reified T> get(url: String): T =
        withContext(coroutineContextProvider.io) {
            mapper.map<T>(
                Jsoup.parse(
                    URL(url),
                    BuildConfig.NETWORK_REQUEST_TIMEOUT_MILLIS
                )
            )
        }
}
