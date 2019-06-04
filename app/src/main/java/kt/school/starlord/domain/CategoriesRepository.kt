package kt.school.starlord.domain

import kt.school.starlord.entity.Category

/**
 * Defines access methods for categories.
 */
interface CategoriesRepository {

    /**
     * Downloads data that represents categories used by the application.
     *
     * @return categories of products and services
     */
    suspend fun getCategories(): List<Category>
}