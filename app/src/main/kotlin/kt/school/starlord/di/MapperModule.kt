package kt.school.starlord.di

import kt.school.starlord.domain.data.mapper.Converter
import kt.school.starlord.entity.Category
import kt.school.starlord.entity.Subcategory
import kt.school.starlord.model.data.mapper.Mapper
import kt.school.starlord.model.data.room.entity.RoomCategory
import kt.school.starlord.model.data.room.entity.RoomSubcategory
import org.koin.dsl.module

/**
 * Contains instructions of how to instantiate Mapper.
 */
val mapperModule = module {
    single { Mapper(converters) }
}

val converters: Set<Converter<*, *>> = setOf(
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
            RoomSubcategory(
                value.name,
                value.categoryName,
                value.count,
                value.link
            )
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
                categoryName = "undefined",
                count = """<sup>((.|\n)*?)</sup>""".toRegex()
                    .find(value.value)?.groups?.get(1)?.value.toString().trim().toInt(),
                link = """<a href="((.|\n)*?)"""".toRegex()
                    .find(value.value)?.groups?.get(1)?.value.toString()
            )
        }
    }
)

private abstract class BaseConverter<FROM, TO>(
    override val fromClass: Class<FROM>,
    override val toClass: Class<TO>
) : Converter<FROM, TO>
