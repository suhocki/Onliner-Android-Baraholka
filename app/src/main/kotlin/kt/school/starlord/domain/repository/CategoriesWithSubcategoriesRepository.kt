package kt.school.starlord.domain.repository

import kt.school.starlord.entity.category.CategoriesWithSubcategories

/**
 * Defines access methods for objects representing pairs of categories with subcategories.
 */
interface CategoriesWithSubcategoriesRepository {

    /**
     * Loads CategoriesWithSubcategories.
     *
     * @return categories with appropriate subcategories.
     * @throws NotImplementedError in case of missing implementation.
     */
    suspend fun getCategoriesWithSubcategories(): CategoriesWithSubcategories
}
