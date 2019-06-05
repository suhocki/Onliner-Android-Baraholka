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
        return withContext(Dispatchers.IO) {
            val page: String = pageLoader.getPage()
            withContext(Dispatchers.Default) {
                parser.parsePage(page)
            }
        }
    }
}