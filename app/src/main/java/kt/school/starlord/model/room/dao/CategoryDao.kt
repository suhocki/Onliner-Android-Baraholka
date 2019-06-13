package kt.school.starlord.model.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kt.school.starlord.model.room.entity.RoomCategory

@Dao
interface CategoryDao {
    @Query("SELECT * FROM Categories")
    suspend fun getCategories(): List<RoomCategory>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun putCategories(categories: List<RoomCategory>)

    @Query("DELETE FROM Categories")
    suspend fun deleteAll()
}