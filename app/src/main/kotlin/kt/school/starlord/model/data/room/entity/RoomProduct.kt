package kt.school.starlord.model.data.room.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kt.school.starlord.domain.entity.product.ProductOwner
import kt.school.starlord.domain.entity.product.ProductPrice
import kt.school.starlord.domain.entity.product.ProductType
import kt.school.starlord.model.data.room.AppDatabase

/**
 * Keeps information about product.
 */
@Entity(
    tableName = AppDatabase.Table.PRODUCTS,
    indices = [
        Index(RoomProduct.SUBCATEGORY_NAME),
        Index(RoomProduct.LAST_UPDATE)
    ]
)
data class RoomProduct(
    @PrimaryKey val id: Long,
    @ForeignKey(
        entity = RoomSubcategory::class,
        parentColumns = [RoomSubcategory.NAME],
        childColumns = [SUBCATEGORY_NAME]
    )
    val subcategoryName: String,
    val title: String,
    val description: String,
    val type: ProductType,
    val location: String,
    val image: String,
    val owner: ProductOwner,
    val price: ProductPrice,
    val lastUpdate: Long,
    val commentsCount: Long,
    val isPaid: Boolean = false
) {
    companion object {
        const val SUBCATEGORY_NAME = "subcategoryName"
        const val LAST_UPDATE = "lastUpdate"
    }
}
