package kt.school.starlord.ui.global.entity

/**
 * Use for generic DiffCallback.
 */
interface UiEntity {
    /**
     * Called to check whether two objects represent the same item.
     */
    fun isItemTheSame(newItem: UiEntity): Boolean

    /**
     * Called to check whether two items have the same data.
     */
    fun isContentTheSame(newItem: UiEntity): Boolean
}
