package kt.school.starlord.model.data.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kt.school.starlord.model.data.room.entity.RoomSubcategory

/**
 * Defines queries for working with subcategories in database.
 */
@Dao
abstract class SubcategoryDao {
    /**
     * @param categoryName selected category.
     * @return all subcategories for selected category.
     */
    @Query("SELECT * FROM Subcategories WHERE categoryName=:categoryName")
    abstract fun getSubcategories(categoryName: String): LiveData<List<RoomSubcategory>>

    /**
     * @param subcategories subcategories that will be saved in database.
     * Previous subcategories will be dropped.
     */
    @Transaction
    open suspend fun replaceAll(subcategories: List<RoomSubcategory>) {
        deleteAll()
        putSubcategories(subcategories)
    }

    @Insert
    protected abstract suspend fun putSubcategories(subcategories: List<RoomSubcategory>)

    @Query("DELETE FROM Subcategories")
    protected abstract suspend fun deleteAll()
}
