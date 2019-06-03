package kt.school.starlord.domain

import kt.school.starlord.entity.Category

interface CategoriesRepository {
    suspend fun getCategories(): List<Category>
}