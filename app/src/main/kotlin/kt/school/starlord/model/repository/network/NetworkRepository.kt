package kt.school.starlord.model.repository.network

import kotlinx.coroutines.withContext
import kt.school.starlord.BuildConfig
import kt.school.starlord.domain.data.mapper.Mapper
import kt.school.starlord.domain.entity.category.Category
import kt.school.starlord.domain.entity.product.Product
import kt.school.starlord.domain.entity.subcategory.Subcategory
import kt.school.starlord.domain.repository.CategoriesWithSubcategoriesRepository
import kt.school.starlord.domain.repository.product.ProductsRepository
import kt.school.starlord.domain.system.coroutine.CoroutineContextProvider
import org.jsoup.Jsoup
import java.net.URL

/**
 * Fetch data through the Internet connection.
 */
class NetworkRepository(
    private val mapper: Mapper,
    private val coroutineContextProvider: CoroutineContextProvider
) : CategoriesWithSubcategoriesRepository, ProductsRepository {

    override suspend fun getCategoriesWithSubcategories(): Map<Category, List<Subcategory>> =
        withContext(coroutineContextProvider.io) {
            Jsoup.parse(URL(BuildConfig.BARAHOLKA_ONLINER_URL), BuildConfig.NETWORK_REQUEST_TIMEOUT_MILLIS)
                .getElementsByClass(CATEGORIES)
                .map { element ->
                    val category: Category = mapper.map(element)
                    val subcategories = element.getElementsByClass(SUBCATEGORIES)
                        .first()
                        .select(SUBCATEGORIES_DATA)
                        .map { mapper.map<Subcategory>(it) }
                        .apply { forEach { subcategory -> subcategory.categoryName = category.name } }

                    category to subcategories
                }
                .toMap()
        }

    override suspend fun getProducts(link: String) = withContext(coroutineContextProvider.io) {
        Jsoup.parse(mapper.map<URL>(link), BuildConfig.NETWORK_REQUEST_TIMEOUT_MILLIS)
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
        private const val CATEGORIES = "cm-onecat"

        private const val SUBCATEGORIES = "b-cm-list"
        private const val SUBCATEGORIES_DATA = "li"

        private const val PRODUCT_CLASS = "ba-tbl-list__table"
        private const val PRODUCT_TAG = "tr"
        private const val PRODUCT_TITLE = "wraptxt"
        private const val PRODUCT_SIGNATURE = "ba-signature"
        private const val PRODUCT_COST = "cost"
    }
}
