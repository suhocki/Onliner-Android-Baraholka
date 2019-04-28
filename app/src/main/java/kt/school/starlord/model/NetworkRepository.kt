package kt.school.starlord.model

import kt.school.starlord.domain.CategoriesRepository
import kt.school.starlord.entity.Category


/*inject module by constructor*/
class NetworkRepository (private val pageLoader: PageLoader): CategoriesRepository {
    override fun getCategories(): List<Category> {
        val page: String = pageLoader.getPage()
        val parser = PageParser()
        return parser.parsePage(page)
    }
}