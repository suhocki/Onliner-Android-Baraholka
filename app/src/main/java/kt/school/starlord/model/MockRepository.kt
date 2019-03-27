package kt.school.starlord.model

import kt.school.starlord.domain.CategoriesRepository
import kt.school.starlord.domain.PageParser
import kt.school.starlord.entity.Category

class MockRepository : CategoriesRepository {

    override fun getCategories(): List<Category> {
        val parser = PageParser()
        return parser.allCategories
    }
}