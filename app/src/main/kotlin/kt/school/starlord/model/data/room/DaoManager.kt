package kt.school.starlord.model.data.room

import kt.school.starlord.model.data.room.dao.CategoryDao
import kt.school.starlord.model.data.room.dao.ProductDao
import kt.school.starlord.model.data.room.dao.SubcategoryDao

/**
 * Provides access to all data access objects (Dao`s) from one point.
 */
data class DaoManager(
    val categoryDao: CategoryDao,
    val subcategoryDao: SubcategoryDao,
    val productDao: ProductDao
)
