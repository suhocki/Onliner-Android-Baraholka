package kt.school.starlord.model.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kt.school.starlord.model.room.entity.RoomSubcategory

@Dao
interface SubcategoryDao {
    @Query("SELECT * FROM Subcategories WHERE categoryName=:categoryName")
    fun getSubcategories(categoryName: String): LiveData<List<RoomSubcategory>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun putSubcategories(subcategories: List<RoomSubcategory>)

    @Query("DELETE FROM Subcategories")
    suspend fun deleteAll()
}