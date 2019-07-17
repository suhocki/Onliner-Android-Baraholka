package kt.school.starlord.model.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import kt.school.starlord.model.data.room.dao.CategoryDao
import kt.school.starlord.model.data.room.dao.SubcategoryDao
import kt.school.starlord.model.data.room.entity.RoomCategory
import kt.school.starlord.model.data.room.entity.RoomSubcategory

/**
 * Defines access methods to data access objects (Dao`s) that will be implemented automatically by Room library.
 */
@Database(
    version = AppDatabase.DATABASE_VERSION,
    entities = [
        RoomCategory::class,
        RoomSubcategory::class
    ]
)
abstract class AppDatabase : RoomDatabase() {
    /**
     * Provides access to CategoryDao.
     */
    abstract fun categoryDao(): CategoryDao

    /**
     * Provides access to SubcategoryDao.
     */
    abstract fun subcategoryDao(): SubcategoryDao

    companion object {
        const val DATABASE_VERSION = 1
    }

    /**
     * Contains constants for database table naming.
     */
    object Table {
        const val CATEGORIES = "Categories"
        const val SUBCATEGORIES = "Subcategories"
    }
}
