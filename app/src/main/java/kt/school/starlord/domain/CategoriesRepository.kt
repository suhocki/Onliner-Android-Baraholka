package kt.school.starlord.domain

import kt.school.starlord.entity.Category

interface CategoriesRepository {
    fun getCategories(): List<Category>
}