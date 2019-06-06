package kt.school.starlord.model

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kt.school.starlord.domain.CategoriesRepository
import kt.school.starlord.entity.Category

/**
 * Implement methods using Internet connection
 */
class NetworkRepository(
    private val pageLoader: PageLoader,
    private val parser: PageParser
) : CategoriesRepository {

    override suspend fun getCategories(): List<Category> {
        val page = withContext(Dispatchers.IO) { pageLoader.getPage() }
        return withContext(Dispatchers.Default) { parser.parsePage(page) }
    }
}