package kt.school.starlord.domain

import androidx.lifecycle.LiveData
import kt.school.starlord.entity.Subcategory

/**
 * Defines access methods for subcategories.
 */
interface SubcategoriesRepository {

    /**
     * Loads data that represents subcategories for current categoryName.
     *
     * @return subcategories
     */
    fun getSubcategories(categoryName: String): LiveData<List<Subcategory>>

    /**
     * Deletes existing subcategories and puts new ones.
     */
    suspend fun updateSubcategories(subcategories: List<Subcategory>)
}
