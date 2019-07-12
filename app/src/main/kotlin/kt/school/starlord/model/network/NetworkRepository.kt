package kt.school.starlord.model.network

import kotlinx.coroutines.withContext
import kt.school.starlord.domain.CategoriesWithSubcategoriesRepository
import kt.school.starlord.domain.CoroutineContextProvider
import kt.school.starlord.entity.Category
import kt.school.starlord.entity.Subcategory
import kt.school.starlord.model.parser.PageParser
import kt.school.starlord.model.urlconnection.PageLoader

/**
 * Implement methods using Internet connection.
 */
class NetworkRepository(
    private val pageLoader: PageLoader,
    private val parser: PageParser,
    private val coroutineContextProvider: CoroutineContextProvider
) : CategoriesWithSubcategoriesRepository {

    override suspend fun getCategoriesWithSubcategories(): Map<Category, List<Subcategory>> {
        val page = withContext(coroutineContextProvider.io) { pageLoader.getPage() }
        return withContext(coroutineContextProvider.default) { parser.parseCategories(page) }
    }
}
