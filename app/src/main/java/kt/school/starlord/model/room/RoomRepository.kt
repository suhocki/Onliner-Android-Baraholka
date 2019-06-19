package kt.school.starlord.model.room

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import kt.school.starlord.domain.SubcategoriesRepository
import kt.school.starlord.entity.Category
import kt.school.starlord.entity.Subcategory
import kt.school.starlord.model.mapper.Mapper
import kt.school.starlord.model.room.entity.RoomCategory
import kt.school.starlord.model.room.entity.RoomSubcategory

class RoomRepository(
    private val daoManager: DaoManager,
    private val mapper: Mapper
) : SubcategoriesRepository {

    /**
     * Loads data that represents categories used by the application.
     *
     * @return categories of products and services
     */
    suspend fun getCategories(): List<Category> {
        val roomCategories = daoManager.categoryDao.getCategories()
        return roomCategories.map { mapper.map<Category>(it) }
    }

    /**
     * Deletes existing categories and puts new ones
     */
    suspend fun updateCategories(categories: List<Category>) {
        val roomCategories = categories.map { mapper.map<RoomCategory>(it) }
        daoManager.categoryDao.deleteAll()
        daoManager.categoryDao.putCategories(roomCategories)
    }

    override fun getSubcategories(categoryName: String): LiveData<List<Subcategory>> {
        val subcategoriesLiveData = daoManager.subcategoryDao.getSubcategories(categoryName)
        return subcategoriesLiveData.map { roomSubcategories ->
            roomSubcategories.map { mapper.map<Subcategory>(it) }
        }
    }

    /**
     * Deletes existing subcategories and puts new ones
     */
    suspend fun updateSubcategories(subcategories: List<Subcategory>) {
        val roomSubcategories = subcategories.map { mapper.map<RoomSubcategory>(it) }
        daoManager.subcategoryDao.deleteAll()
        daoManager.subcategoryDao.putSubcategories(roomSubcategories)
    }
}