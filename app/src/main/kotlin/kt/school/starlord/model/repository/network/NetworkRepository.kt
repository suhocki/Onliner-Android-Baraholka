package kt.school.starlord.model.repository.network

import kotlinx.coroutines.withContext
import kt.school.starlord.domain.repository.CategoriesWithSubcategoriesRepository
import kt.school.starlord.domain.system.coroutine.CoroutineContextProvider
import kt.school.starlord.entity.Category
import kt.school.starlord.entity.Subcategory
import kt.school.starlord.model.data.network.OnlinerApi
import kt.school.starlord.model.data.parser.PageParser

/**
 * Implement methods using Internet connection.
 */
class NetworkRepository(
    private val pageLoader: OnlinerApi,
    private val parser: PageParser,
    private val coroutineContextProvider: CoroutineContextProvider
) : CategoriesWithSubcategoriesRepository {

    override suspend fun getCategoriesWithSubcategories(): Map<Category, List<Subcategory>> {
        val page = withContext(coroutineContextProvider.io) { pageLoader.loadPage() }
        return withContext(coroutineContextProvider.default) { parser.parseCategories(page) }
    }
}
