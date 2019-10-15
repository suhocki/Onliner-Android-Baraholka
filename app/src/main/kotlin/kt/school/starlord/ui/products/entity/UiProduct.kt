package kt.school.starlord.ui.products.entity

import androidx.annotation.ColorRes
import kt.school.starlord.ui.global.annotation.Visibility

/**
 *  Product entity for UI purposes.
 */
data class UiProduct(
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
)
