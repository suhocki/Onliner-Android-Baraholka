package kt.school.starlord.model.data.room.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kt.school.starlord.entity.product.ProductOwner
import kt.school.starlord.entity.product.ProductPrice
import kt.school.starlord.entity.product.ProductType
import kt.school.starlord.model.data.room.AppDatabase

/**
 * Keeps information about product.
 *
 * @param position defines a position of product in the resulted list. When we load products from database
 * there is a big chance that the order of the elements will be not the same as it on the web server.
 */
@Entity(
    tableName = AppDatabase.Table.PRODUCTS,
    indices = [
        Index(RoomProduct.SUBCATEGORY_NAME),
        Index(RoomProduct.POSITION)
    ]
)
data class RoomProduct(
    @PrimaryKey val id: Long,
    val position: Int,
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
    val lastUpdate: String,
    val commentsCount: Long,
    val isPaid: Boolean = false
) {
    companion object {
        const val SUBCATEGORY_NAME = "subcategoryName"
        const val POSITION = "position"
    }
}
