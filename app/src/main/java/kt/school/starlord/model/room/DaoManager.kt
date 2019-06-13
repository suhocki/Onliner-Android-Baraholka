package kt.school.starlord.model.room

import kt.school.starlord.model.room.dao.CategoryDao
import kt.school.starlord.model.room.dao.SubcategoryDao

data class DaoManager(
    val categoryDao: CategoryDao,
    val subcategoryDao: SubcategoryDao
)