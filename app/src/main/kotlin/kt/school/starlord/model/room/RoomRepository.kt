package kt.school.starlord.model.room

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import kt.school.starlord.domain.SubcategoriesRepository
import kt.school.starlord.entity.Category
import kt.school.starlord.entity.Subcategory
import kt.school.starlord.model.mapper.Mapper
import kt.school.starlord.model.room.entity.RoomCategory
import kt.school.starlord.model.room.entity.RoomSubcategory

/**
 * Controls Room database.
 */
class RoomRepository(
    private val daoManager: DaoManager,
    private val mapper: Mapper
) : SubcategoriesRepository {

    /**
     * Loads data that represents categories used by the application.
     *
     * @return categories of products and services
     */
    fun getCategories(): LiveData<List<Category>> {
        return daoManager.categoryDao.getCategories().map { roomCategories ->
            roomCategories.map { mapper.map<Category>(it) }
        }
    }

    /**
     * Deletes existing categories and puts new ones.
     */
    suspend fun updateCategories(categories: List<Category>) {
        val roomCategories = categories.map { mapper.map<RoomCategory>(it) }
        daoManager.categoryDao.replaceAll(roomCategories)
    }

    override fun getSubcategories(categoryName: String): LiveData<List<Subcategory>> {
        return daoManager.subcategoryDao.getSubcategories(categoryName).map { roomSubcategories ->
            roomSubcategories.map { mapper.map<Subcategory>(it) }
        }
    }

    /**
     * Deletes existing subcategories and puts new ones.
     */
    suspend fun updateSubcategories(subcategories: List<Subcategory>) {
        val roomSubcategories = subcategories.map { mapper.map<RoomSubcategory>(it) }
        daoManager.subcategoryDao.deleteAll()
        daoManager.subcategoryDao.putSubcategories(roomSubcategories)
    }
}
