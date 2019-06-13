package kt.school.starlord.model.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kt.school.starlord.model.room.AppDatabase


@Entity(tableName = AppDatabase.Table.CATEGORIES)
data class RoomCategory(
    @PrimaryKey val name: String
) {
    companion object {
        const val NAME = "name"
    }
}