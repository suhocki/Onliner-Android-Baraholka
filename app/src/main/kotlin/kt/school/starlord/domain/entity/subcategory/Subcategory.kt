package kt.school.starlord.domain.entity.subcategory

/**
 * Entity that appears on subcategories screen.
 *
 * @param id unique part of Url for navigation to selected subcategory products
 */
data class Subcategory(
    val name: String,
    val count: Int?,
    val id: Long,
    var categoryName: String? = null
)
