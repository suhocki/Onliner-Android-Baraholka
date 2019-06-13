package kt.school.starlord.model.room.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kt.school.starlord.model.room.AppDatabase

@Entity(
    tableName = AppDatabase.Table.SUBCATEGORIES,
    indices = [(Index(RoomSubcategory.CATEGORY_NAME))]
)
data class RoomSubcategory(
    @PrimaryKey
    val name: String,
    @ForeignKey(
        entity = RoomCategory::class,
        parentColumns = [RoomCategory.NAME],
        childColumns = [CATEGORY_NAME]
    )
    val categoryName: String,
    val count: Int,
    val link: String
) {
    companion object {
        const val CATEGORY_NAME = "categoryName"
    }
}