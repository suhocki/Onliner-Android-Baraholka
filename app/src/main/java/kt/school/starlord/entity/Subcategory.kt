package kt.school.starlord.entity

/**
 * Entity that appears on subcategories screen.
 */
data class Subcategory(
    val name: String,
    var categoryName: String,
    val count: Int,
    val link: String
)
