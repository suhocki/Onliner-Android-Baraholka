package kt.school.starlord.model

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kt.school.starlord.domain.CategoriesRepository
import kt.school.starlord.entity.Category

class NetworkRepository(
    private val pageLoader: PageLoader,
    private val parser: PageParser
) : CategoriesRepository {

    override suspend fun getCategories(): List<Category> = withContext(Dispatchers.IO) {
        val page: String = pageLoader.getPage()
        return@withContext parser.parsePage(page)
    }
}