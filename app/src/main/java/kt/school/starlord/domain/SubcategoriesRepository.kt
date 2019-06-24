package kt.school.starlord.domain

import kt.school.starlord.entity.Subcategory

/**
 * Defines access methods for subcategories.
 */
interface SubcategoriesRepository {

    /**
     * Loads data that represents subcategories for current categoryName.
     *
     * @return subcategories
     */
    suspend fun getSubcategories(categoryName: String): List<Subcategory>
}
