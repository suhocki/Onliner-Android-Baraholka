package kt.school.starlord.model.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kt.school.starlord.model.room.entity.RoomCategory

/**
 * Defines queries for working with categories in database.
 */
@Dao
abstract class CategoryDao {
    /**
     * @return all categories.
     */
    @Query("SELECT * FROM Categories")
    abstract fun getCategories(): LiveData<List<RoomCategory>>

    /**
     * Replaces all categories from database.
     * @param categories categories that will be saved in database.
     */
    @Transaction
    open suspend fun replaceAll(categories: List<RoomCategory>)  {
        deleteAll()
        putCategories(categories)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract suspend fun putCategories(categories: List<RoomCategory>)

    @Query("DELETE FROM Categories")
    protected abstract suspend fun deleteAll()
}
