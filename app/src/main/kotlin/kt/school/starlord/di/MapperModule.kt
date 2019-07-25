package kt.school.starlord.di

import kt.school.starlord.entity.Category
import kt.school.starlord.entity.Subcategory
import kt.school.starlord.model.data.mapper.Mapper
import kt.school.starlord.model.data.mapper.converter.DocumentToCategoriesWithSubcategoriesConverter
import kt.school.starlord.model.data.mapper.entity.BaseConverter
import kt.school.starlord.model.data.room.entity.RoomCategory
import kt.school.starlord.model.data.room.entity.RoomSubcategory
import org.koin.dsl.module

/**
 * Contains instructions of how to instantiate Mapper.
 */
val mapperModule = module {
    single {
        val converters = setOf(
            DocumentToCategoriesWithSubcategoriesConverter(),
            object : BaseConverter<RoomCategory, Category>(RoomCategory::class.java, Category::class.java) {
                override fun convert(value: RoomCategory) = Category(value.name)
            },
            object : BaseConverter<Category, RoomCategory>(Category::class.java, RoomCategory::class.java) {
                override fun convert(value: Category) =
                    RoomCategory(name = value.name)
            },
            object : BaseConverter<RoomSubcategory, Subcategory>(RoomSubcategory::class.java, Subcategory::class.java) {
                override fun convert(value: RoomSubcategory) =
                    Subcategory(value.name, value.categoryName, value.count, value.link)
            },
            object : BaseConverter<Subcategory, RoomSubcategory>(Subcategory::class.java, RoomSubcategory::class.java) {
                override fun convert(value: Subcategory) =
                    RoomSubcategory(value.name, value.categoryName, value.count, value.link)
            }
        )
        Mapper(converters)
    }
}
