package kt.school.starlord.domain

import kt.school.starlord.entity.Category
import kt.school.starlord.entity.Subcategory

/**
 * Defines access methods for categories.
 */
interface CategoriesRepository {

    /**
     * Loads data that represents categories used by the application.
     *
     * @return categories of products and services
     * @throws NotImplementedError in case of missing implementation
     */
    suspend fun getCategories(): List<Category> {
        throw NotImplementedError()
    }

    /**
     * Loads map were keys are categories and values are subcategories.
     *
     * @return categories with appropriate subcategories.
     * @throws NotImplementedError in case of missing implementation.
     */
    suspend fun getCategoriesWithSubcategories(): Map<Category, List<Subcategory>> {
        throw NotImplementedError()
    }

    /**
     * Deletes existing categories and puts new ones
     */
    suspend fun updateCategories(categories: List<Category>) {}
}