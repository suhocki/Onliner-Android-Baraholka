package kt.school.starlord.model.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kt.school.starlord.domain.CategoriesRepository
import kt.school.starlord.entity.Category
import kt.school.starlord.entity.Subcategory
import kt.school.starlord.model.httpurlconnection.PageLoader
import kt.school.starlord.model.parser.PageParser

/**
 * Implement methods using Internet connection
 */
class NetworkRepository(
    private val pageLoader: PageLoader,
    private val parser: PageParser
) : CategoriesRepository {

    override suspend fun getCategoriesWithSubcategories(): Map<Category, List<Subcategory>> {
        val page = withContext(Dispatchers.IO) { pageLoader.getPage() }
        return withContext(Dispatchers.Default) { parser.parseCategories(page) }
    }
}