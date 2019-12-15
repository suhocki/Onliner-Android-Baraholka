package kt.school.starlord.domain.entity.product

import kt.school.starlord.ui.global.entity.wrapper.LocalizedTimePassed

/**
 * Domain product entity.
 * Can represents not only products but LoadMore and Progress.
 *
 * @param lastUpdate time when the product was updated.
 * @param localizedTimePassed localized human-readable time elapsed after the product was updated.
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
    var lastUpdate: Long,
    val localizedTimePassed: LocalizedTimePassed,
    val commentsCount: Long,
    var subcategoryId: Long? = null,
    val isPaid: Boolean = false
)
