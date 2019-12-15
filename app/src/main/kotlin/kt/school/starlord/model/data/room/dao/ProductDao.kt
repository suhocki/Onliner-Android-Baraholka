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
     * Get all products that are connected with chosen subcategory name, sorted desc, only unpaid.
     *
     * @param subcategoryId name of selected subcategory.
     * @return all products for selected subcategory.
     */
    @Query("SELECT * FROM Products WHERE subcategoryId=:subcategoryId AND isPaid=:isPaid ORDER BY lastUpdate DESC")
    fun getProductsSortedByUpdate(
        subcategoryId: Long,
        isPaid: Boolean = false
    ): DataSource.Factory<Int, RoomProduct>

    /**
     * Find the product with max last update value and return it`s lastUpdate.
     */
    @Query("SELECT MAX(lastUpdate) FROM Products WHERE subcategoryId=:subcategoryId")
    suspend fun getMaxLastUpdate(subcategoryId: Long): Long?

    /**
     * New products will be added to the database. Existed products will be replaced.
     *
     * @param products items to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<RoomProduct>)

    /**
     * Extract all items where identifier is from the listOfIds.
     *
     * @param listOfIds collection of identifiers.
     * @return all items where identifier is from the listOfIds.
     */
    @Query("SELECT * FROM Products WHERE subcategoryId=:subcategoryId AND id IN (:listOfIds)")
    suspend fun getProductsByIds(listOfIds: List<Long>, subcategoryId: Long): List<RoomProduct>

    /**
     * Delete item by id.
     */
    @Query("DELETE FROM Products WHERE id = :id")
    suspend fun deleteProductById(id: Long)
}
