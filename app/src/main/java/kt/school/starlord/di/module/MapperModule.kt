package kt.school.starlord.di.module

import kt.school.starlord.entity.Category
import kt.school.starlord.entity.Subcategory
import kt.school.starlord.model.mapper.BaseConverter
import kt.school.starlord.model.mapper.Mapper
import kt.school.starlord.model.room.entity.RoomCategory
import kt.school.starlord.model.room.entity.RoomSubcategory
import org.koin.dsl.module

/**
 * Contains instructions of how to instantiate Mapper
 */
val mapperModule = module {
    single { Mapper(converters) }
}

private val converters = setOf(
    object : BaseConverter<RoomCategory, Category>(RoomCategory::class.java, Category::class.java) {
        override fun convert(value: RoomCategory) = Category(value.name)
    },
    object : BaseConverter<Category, RoomCategory>(Category::class.java, RoomCategory::class.java) {
        override fun convert(value: Category) = RoomCategory(name = value.name)
    },
    object : BaseConverter<RoomSubcategory, Subcategory>(
        RoomSubcategory::class.java,
        Subcategory::class.java
    ) {
        override fun convert(value: RoomSubcategory) =
            Subcategory(value.name, count = value.count, link = value.link)
    },
    object : BaseConverter<Subcategory, RoomSubcategory>(
        Subcategory::class.java,
        RoomSubcategory::class.java
    ) {
        override fun convert(value: Subcategory): RoomSubcategory {
            return RoomSubcategory(
                categoryName = value.requireCategoryName(),
                name = value.name,
                count = value.count,
                link = value.link
            )
        }
    },
    object : BaseConverter<MatchResult, Category>(MatchResult::class.java, Category::class.java) {
        override fun convert(value: MatchResult): Category {
            val regex = """<h3>((.|\n)*?)</h3>""".toRegex()
            return Category(regex.find(value.value)?.groups?.get(1)?.value.toString())
        }
    },
    object : BaseConverter<MatchResult, Subcategory>(MatchResult::class.java, Subcategory::class.java) {
        override fun convert(value: MatchResult): Subcategory {
            return Subcategory(
                name = """">((.|\n)*?)</a""".toRegex()
                    .find(value.value)?.groups?.get(1)?.value.toString(),
                count = """<sup>((.|\n)*?)</sup>""".toRegex()
                    .find(value.value)?.groups?.get(1)?.value.toString().trim().toInt(),
                link = """<a href="((.|\n)*?)"""".toRegex()
                    .find(value.value)?.groups?.get(1)?.value.toString()
            )
        }
    }
)
