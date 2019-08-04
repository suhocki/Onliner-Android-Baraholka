package kt.school.starlord.model.data.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
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
    @Query("SELECT * FROM Products WHERE subcategoryName=:subcategoryName")
    fun getProducts(subcategoryName: String): LiveData<List<RoomProduct>>
}
