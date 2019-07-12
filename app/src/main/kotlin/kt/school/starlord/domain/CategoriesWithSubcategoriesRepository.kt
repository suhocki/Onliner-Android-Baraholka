package kt.school.starlord.domain

import kt.school.starlord.entity.Category
import kt.school.starlord.entity.Subcategory

/**
 * Defines access methods for objects representing pairs of categories with subcategories.
 */
interface CategoriesWithSubcategoriesRepository {

    /**
     * Loads map were keys are categories and values are subcategories.
     *
     * @return categories with appropriate subcategories.
     * @throws NotImplementedError in case of missing implementation.
     */
    suspend fun getCategoriesWithSubcategories(): Map<Category, List<Subcategory>>
}
