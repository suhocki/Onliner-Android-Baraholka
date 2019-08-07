package kt.school.starlord.entity.product

/**
 * Entity for RecyclerView at ProductsFragment.
 * Can represents not only products but services.
 *
 * @param isPaid product is premium item. Should be located in the top of the list.
 */
data class Product(
    val id: Long,
    val title: String,
    val description: String,
    val type: ProductType,
    val location: String,
    val image: String,
    val owner: ProductOwner,
    val price: ProductPrice,
    val lastUpdate: String,
    val commentsCount: Long,
    val isPaid: Boolean = false
)
