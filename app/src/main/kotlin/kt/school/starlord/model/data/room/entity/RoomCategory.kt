package kt.school.starlord.model.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kt.school.starlord.model.data.room.StarlordDatabase

/**
 * Keeps information about name of category.
 * @param name primary key in database table
 */
@Entity(tableName = StarlordDatabase.Table.CATEGORIES)
data class RoomCategory(
    @PrimaryKey val name: String
) {
    companion object {
        const val NAME = "name"
    }
}
