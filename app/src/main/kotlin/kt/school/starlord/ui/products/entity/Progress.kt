package kt.school.starlord.ui.products.entity

import kt.school.starlord.ui.global.entity.UiEntity

/**
 * Ui item that represents progress view.
 */
class Progress: UiEntity {
    override fun isItemTheSame(newItem: UiEntity) = newItem is Progress

    override fun isContentTheSame(newItem: UiEntity) = true
}