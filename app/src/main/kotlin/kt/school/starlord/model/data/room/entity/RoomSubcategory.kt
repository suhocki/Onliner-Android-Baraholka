package kt.school.starlord.model.data.room.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kt.school.starlord.model.data.room.StarlordDatabase

/**
 * Database entity that keeps info about Subcategory.
 * @param id primary key in Subcategories table. Also determines info for navigation to specific forum.
 * @param categoryName foreign key from Categories table
 * @param count describes how much products under this subcategory.
 */
@Entity(
    tableName = StarlordDatabase.Table.SUBCATEGORIES,
    indices = [Index(RoomSubcategory.CATEGORY_NAME)]
)
data class RoomSubcategory(
    @PrimaryKey val id: Long,
    @ForeignKey(
        entity = RoomCategory::class,
        parentColumns = [RoomCategory.NAME],
        childColumns = [CATEGORY_NAME]
    )
    val categoryName: String,
    val name: String,
    val count: Int?
) {
    companion object {
        const val ID = "id"
        const val CATEGORY_NAME = "categoryName"
    }
}
