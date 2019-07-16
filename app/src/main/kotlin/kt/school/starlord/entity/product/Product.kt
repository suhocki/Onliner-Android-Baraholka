package kt.school.starlord.entity.product

import kt.school.starlord.entity.ProductOwner

/**
 * Entity for RecyclerView at ProductsFragment.
 * Can represents not only products but services
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
    val isPaid : Boolean = false
)
