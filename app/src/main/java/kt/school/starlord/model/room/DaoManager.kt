package kt.school.starlord.model.room

import kt.school.starlord.model.room.dao.CategoryDao
import kt.school.starlord.model.room.dao.SubcategoryDao

/**
 * Provides access to all data access objects (Dao`s) from one point
 */
data class DaoManager(
    val categoryDao: CategoryDao,
    val subcategoryDao: SubcategoryDao
)