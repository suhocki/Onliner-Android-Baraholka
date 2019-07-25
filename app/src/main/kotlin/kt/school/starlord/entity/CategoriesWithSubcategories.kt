package kt.school.starlord.entity

/**
 * Entity that is used for loading pairs of categories with appropriate subcategories.
 * Created to simplify object type.
 *
 * @param data is a List of pairs categories with subcategories.
 */
data class CategoriesWithSubcategories(
    val categories: List<Category>,
    val subcategories: List<Subcategory>
)
