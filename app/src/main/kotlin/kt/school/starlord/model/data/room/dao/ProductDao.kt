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
     * Extract all products from the database that are connected with chosen subcategory name.
     *
     * @param subcategoryName name of selected subcategory.
     * @return all products for selected subcategory.
     */
    @Query("SELECT * FROM Products WHERE subcategoryName=:subcategoryName ORDER BY lastUpdate DESC")
    fun getProducts(subcategoryName: String): DataSource.Factory<Int, RoomProduct>

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
    @Query("SELECT * FROM Products WHERE subcategoryName=:subcategoryName AND id IN (:listOfIds)")
    suspend fun getProductsByIds(listOfIds: List<Long>, subcategoryName: String): List<RoomProduct>
}
