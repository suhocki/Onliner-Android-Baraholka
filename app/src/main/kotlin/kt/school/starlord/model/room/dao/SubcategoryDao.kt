package kt.school.starlord.model.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kt.school.starlord.model.room.entity.RoomSubcategory

/**
 * Defines queries for working with subcategories in database.
 */
@Dao
interface SubcategoryDao {
    /**
     *  @return all subcategories.
     */
    @Query("SELECT * FROM Subcategories WHERE categoryName=:categoryName")
    fun getSubcategories(categoryName: String): LiveData<List<RoomSubcategory>>

    /**
     * @param subcategories subcategories that will be saved in database.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun putSubcategories(subcategories: List<RoomSubcategory>)

    /**
     * Deletes all subcategories from database.
     */
    @Query("DELETE FROM Subcategories")
    suspend fun deleteAll()
}
