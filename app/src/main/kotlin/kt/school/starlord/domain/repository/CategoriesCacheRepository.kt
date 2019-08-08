package kt.school.starlord.domain.repository

import androidx.lifecycle.LiveData
import kt.school.starlord.entity.category.Category

/**
 * Defines access methods for categories that are located in the local cache.
 */
interface CategoriesCacheRepository {

    /**
     * Loads data that represents categories used by the application.
     *
     * @return categories of products and services
     */
    fun getCategories(): LiveData<List<Category>>

    /**
     * Deletes existing categories and puts new ones.
     */
    suspend fun updateCategories(categories: List<Category>)
}
