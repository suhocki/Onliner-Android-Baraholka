package kt.school.starlord.entity

data class Category(
    val name: String,
    val subCategories: List<Subcategory>
)