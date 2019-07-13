package kt.school.starlord.entity.product

import kt.school.starlord.entity.User

/**
 * Entity for RecyclerView at ProductsFragment.
 * Can represents not only products but services
 */
data class Product(
    val type: ProductType,
    val title: String,
    val description: String,
    val location: String,
    val image: String,
    val user: User,
    val price: Double,
    val lastUpdate: String,
    val commentsCount: String
)
