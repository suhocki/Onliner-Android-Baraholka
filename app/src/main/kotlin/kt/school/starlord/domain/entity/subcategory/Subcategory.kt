package kt.school.starlord.domain.entity.subcategory

/**
 * Entity that appears on subcategories screen.
 *
 * @param link unique part of Url for navigation to selected subcategory products
 */
data class Subcategory(
    val name: String,
    val count: Int?,
    val link: String,
    var categoryName: String? = null
)
