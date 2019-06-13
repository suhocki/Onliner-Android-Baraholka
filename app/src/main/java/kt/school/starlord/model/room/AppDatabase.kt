package kt.school.starlord.model.room

import androidx.room.Database
import androidx.room.RoomDatabase
import kt.school.starlord.model.room.dao.CategoryDao
import kt.school.starlord.model.room.dao.SubcategoryDao
import kt.school.starlord.model.room.entity.RoomCategory
import kt.school.starlord.model.room.entity.RoomSubcategory

@Database(
    version = AppDatabase.DATABASE_VERSION,
    entities = [
        RoomCategory::class,
        RoomSubcategory::class
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun subcategoryDao(): SubcategoryDao

    companion object {
        const val DATABASE_VERSION = 1
    }

    object Table {
        const val CATEGORIES = "Categories"
        const val SUBCATEGORIES = "Subcategories"
    }
}