package kt.school.starlord.model.data.room.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kt.school.starlord.model.data.room.entity.RoomProduct

/**
 * Defines queries for working with products in database.
 */
@Dao
interface ProductDao {
    /**
     * @param subcategoryName name of selected subcategory.
     * @return all products for selected subcategory by subcategoryName.
     */
    @Query("SELECT * FROM Products WHERE subcategoryName=:subcategoryName ORDER BY lastUpdate DESC")
    fun getProducts(subcategoryName: String): DataSource.Factory<Int, RoomProduct>

    /**
     * Replaces old products with a new ones.
     *
     * @param products items that will be saved in database.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun putProducts(products: List<RoomProduct>)
}
