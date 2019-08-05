package kt.school.starlord.entity.product

/**
 * Created to store product together with subcategory name.
 */
data class ProductWithSubcategoryName(
    val product: Product,
    val subcategoryName: String
)
