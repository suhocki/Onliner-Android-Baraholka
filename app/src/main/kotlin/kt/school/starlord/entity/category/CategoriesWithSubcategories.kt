package kt.school.starlord.entity.category

import kt.school.starlord.entity.subcategory.Subcategory

/**
 * Entity that is used for loading pairs of categories with appropriate subcategories.
 * Created to simplify object type.
 */
data class CategoriesWithSubcategories(
    val categories: List<Category>,
    val subcategories: List<Subcategory>
)
