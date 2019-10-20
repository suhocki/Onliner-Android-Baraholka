package kt.school.starlord.model.data.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
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
    @Query("SELECT * FROM Products WHERE subcategoryName=:subcategoryName ORDER BY lastUpdate DESC LIMIT :pageSize")
    abstract fun getProducts(subcategoryName: String, pageSize: Int): LiveData<List<RoomProduct>>

    /**
     * Replaces old products with a new ones. Previous products with provided subcategoryName will be dropped.
     *
     * @param subcategoryName name of selected subcategory.
     * @param products items that will be saved in database.
     */
    @Transaction
    open suspend fun replaceAll(subcategoryName: String, products: List<RoomProduct>) {
        deleteAll(subcategoryName)
        putProducts(products)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract suspend fun putProducts(products: List<RoomProduct>)

    @Query("DELETE FROM Products WHERE subcategoryName=:subcategoryName")
    protected abstract suspend fun deleteAll(subcategoryName: String)
}
