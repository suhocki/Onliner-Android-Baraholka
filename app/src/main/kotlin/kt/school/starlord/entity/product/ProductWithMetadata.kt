package kt.school.starlord.entity.product

/**
 * Created to store product together with subcategory name.
 */
data class ProductWithMetadata(
    val product: Product,
    val subcategoryName: String
)
