package kt.school.starlord.model.data.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kt.school.starlord.model.data.room.entity.RoomProduct

/**
 * Defines queries for working with products in database.
 */
@Dao
abstract class ProductDao {
    /**
     * @param subcategoryName name of selected subcategory.
     * @return all products for selected subcategory by subcategoryName.
     */
    @Query("SELECT * FROM Products WHERE subcategoryName=:subcategoryName")
    abstract fun getProducts(subcategoryName: String): LiveData<List<RoomProduct>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract suspend fun putProducts(products: List<RoomProduct>)

    @Query("DELETE FROM Products WHERE subcategoryName=:subcategoryName")
    protected abstract suspend fun deleteAll(subcategoryName: String)
}
