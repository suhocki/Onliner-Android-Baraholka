package kt.school.starlord.ui.products.entity

import kt.school.starlord.ui.global.entity.UiEntity

/**
 * Ui item that represents "load more" button.
 */
class LoadMore : UiEntity {
    override fun isItemTheSame(newItem: UiEntity) = newItem is LoadMore

    override fun isContentTheSame(newItem: UiEntity) = true
}