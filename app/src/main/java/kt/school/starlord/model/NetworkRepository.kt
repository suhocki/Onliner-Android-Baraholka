package kt.school.starlord.model

import kt.school.starlord.di.PageLoader
import kt.school.starlord.di.PageParser
import kt.school.starlord.domain.CategoriesRepository
import kt.school.starlord.entity.Category

class NetworkRepository : CategoriesRepository {
    override fun getCategories(): List<Category> {
        val page: String = PageLoader().page
        val parser = PageParser()
        return parser.parsePage(page)
    }
}