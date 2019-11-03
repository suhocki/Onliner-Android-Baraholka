package kt.school.starlord.domain.entity.product

import kt.school.starlord.domain.entity.global.Timestamp

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
    val price: Price,
    var timestamp: Timestamp,
    val commentsCount: Long,
    val subcategoryName: String = "", // todo. make it nullable
    val isPaid: Boolean = false
)
