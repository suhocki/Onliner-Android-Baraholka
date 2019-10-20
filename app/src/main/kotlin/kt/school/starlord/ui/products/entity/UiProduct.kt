package kt.school.starlord.ui.products.entity

import androidx.annotation.ColorRes
import kt.school.starlord.ui.global.UiEntity
import kt.school.starlord.ui.global.annotation.Visibility

/**
 *  Product entity for UI purposes.
 *  @param id need for diff callback.
 */
data class UiProduct(
    val id: Long,
    val title: String,
    val description: String,
    val location: String,
    val image: String,
    val owner: String,
    val lastUpdate: String,
    val comments: String,
    val price: String,
    val isPaid: Boolean,
    val type: String,
    @ColorRes val typeColor: Int,
    @Visibility val commentsCountVisibility: Int,
    @Visibility val priceVisibility: Int,
    @Visibility val bargainVisibility: Int
) : UiEntity {
    override fun isItemTheSame(newItem: UiEntity) =
        newItem is UiProduct && id == newItem.id

    override fun isContentTheSame(newItem: UiEntity) =
        newItem == this
}
