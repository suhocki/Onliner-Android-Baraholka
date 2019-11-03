package kt.school.starlord.domain.entity.product

/**
 * Domain product entity.
 * Can represents not only products but services.
 *
 * @param isPaid if true, the product is premium item and should be located at the top of the list.
 */
data class Product(
    val id: Long,
    val title: String,
    val description: String,
    val type: ProductType,
    val location: String,
    val image: String,
    val owner: ProductOwner,
    val price: Price,
    var lastUpdate: LastUpdate,
    val commentsCount: Long,
    val subcategoryName: String = "", // todo. make it nullable
    val isPaid: Boolean = false
)
