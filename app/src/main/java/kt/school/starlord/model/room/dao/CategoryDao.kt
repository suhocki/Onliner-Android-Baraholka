package kt.school.starlord.model.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kt.school.starlord.model.room.entity.RoomCategory

/**
 * Defines queries for working with categories in database
 */
@Dao
interface CategoryDao {
    /**
     * @return all categories
     */
    @Query("SELECT * FROM Categories")
    suspend fun getCategories(): List<RoomCategory>

    /**
     * @param categories categories that will be saved in database
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun putCategories(categories: List<RoomCategory>)

    /**
     * Deletes all categories from database
     */
    @Query("DELETE FROM Categories")
    suspend fun deleteAll()
}