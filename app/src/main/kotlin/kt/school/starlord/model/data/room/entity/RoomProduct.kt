package kt.school.starlord.model.data.room.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kt.school.starlord.domain.entity.product.Price
import kt.school.starlord.domain.entity.product.ProductOwner
import kt.school.starlord.domain.entity.product.ProductType
import kt.school.starlord.model.data.room.StarlordDatabase

/**
 * Keeps information about the product.
 * May represent LoadMore and Progress.
 */
@Entity(
    tableName = StarlordDatabase.Table.PRODUCTS,
    indices = [
        Index(RoomProduct.SUBCATEGORY_ID),
        Index(RoomProduct.LAST_UPDATE)
    ]
)
data class RoomProduct(
    @PrimaryKey val id: Long,
    @ForeignKey(
        entity = RoomSubcategory::class,
        parentColumns = [RoomSubcategory.ID],
        childColumns = [SUBCATEGORY_ID]
    )
    var subcategoryId: Long,
    val title: String,
    val description: String,
    val type: ProductType,
    val location: String,
    val image: String,
    val owner: ProductOwner,
    val price: Price,
    val lastUpdate: Long,
    val commentsCount: Long,
    val isPaid: Boolean = false
) {
    companion object {
        private const val EMPTY_STRING = ""

        const val SUBCATEGORY_ID = "subcategoryId"
        const val LAST_UPDATE = "lastUpdate"
        const val ID_PROGRESS = 1L
        const val ID_LOAD_MORE = 2L

        /**
         * Constructs LoadMore item.
         *
         * @param subcategoryId id of subcategory where the item would be shown.
         * @param lastUpdate determines position in list after sorting by this field.
         */
        fun createLoadMore(subcategoryId: Long, lastUpdate: Long) = createRoomProduct(
            id = ID_LOAD_MORE,
            subcategoryId = subcategoryId,
            lastUpdate = lastUpdate
        )

        /**
         * Constructs Progress item.
         *
         * @param subcategoryId id of subcategory where the item would be shown.
         * @param lastUpdate determines position in list after sorting by this field.
         */
        fun createProgress(subcategoryId: Long, lastUpdate: Long) = createRoomProduct(
            id = ID_PROGRESS,
            subcategoryId = subcategoryId,
            lastUpdate = lastUpdate
        )

        private fun createRoomProduct(id: Long, subcategoryId: Long, lastUpdate: Long) = RoomProduct(
            id = id,
            subcategoryId = subcategoryId,
            lastUpdate = lastUpdate,
            // fields below will never be displayed.
            price = Price(0.0, false, isBargainAvailable = false),
            title = EMPTY_STRING,
            image = EMPTY_STRING,
            location = EMPTY_STRING,
            commentsCount = 0L,
            description = EMPTY_STRING,
            owner = ProductOwner(EMPTY_STRING, 0L),
            type = ProductType.WARNING
        )
    }
}
