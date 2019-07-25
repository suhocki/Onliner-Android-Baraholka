package kt.school.starlord.di

import kt.school.starlord.domain.data.mapper.Converter
import kt.school.starlord.entity.CategoriesWithSubcategories
import kt.school.starlord.entity.Category
import kt.school.starlord.entity.Subcategory
import kt.school.starlord.model.data.mapper.Mapper
import kt.school.starlord.model.data.room.entity.RoomCategory
import kt.school.starlord.model.data.room.entity.RoomSubcategory
import org.jsoup.nodes.Document
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
            RoomSubcategory(value.name, value.categoryName, value.count, value.link)
    },
    object : BaseConverter<Document, CategoriesWithSubcategories>(
        Document::class.java, CategoriesWithSubcategories::class.java
    ) {
        override fun convert(value: Document): CategoriesWithSubcategories {
            val htmlCategories = value.getElementsByClass(HtmlTag.CATEGORIES)
            val categories = mutableListOf<Category>()
            val subcategories = mutableListOf<Subcategory>()

            htmlCategories.forEach {
                val categoryName = it.select(HtmlTag.CATEGORY_NAME).text()

                val htmlSubcategories = it.getElementsByClass(HtmlTag.SUBCATEGORIES)
                    .first()
                    .select(HtmlTag.SUBCATEGORIES_DATA)

                val category = Category(categoryName)

                val subcategoriesOfCategory = htmlSubcategories.map { htmlSubcategory ->
                    val htmlSubcategoryNameAndLink = htmlSubcategory.getElementsByAttribute(HtmlTag.LINK)
                    Subcategory(
                        name = htmlSubcategoryNameAndLink.text(),
                        categoryName = category.name,
                        count = htmlSubcategory.select(HtmlTag.PRODUCTS_COUNT).text().toInt(),
                        link = htmlSubcategoryNameAndLink.attr(HtmlTag.LINK)
                    )
                }

                categories.add(category)
                subcategories.addAll(subcategoriesOfCategory)
            }

            return CategoriesWithSubcategories(categories, subcategories)
        }
    }
)

/**
 * Contains constructor with parameters for simplicity.
 */
abstract class BaseConverter<FROM, TO>(
    override val fromClass: Class<FROM>,
    override val toClass: Class<TO>
) : Converter<FROM, TO>

private object HtmlTag {
    const val CATEGORIES = "cm-onecat"
    const val SUBCATEGORIES = "b-cm-list"
    const val PRODUCTS_COUNT = "sup"
    const val SUBCATEGORIES_DATA = "li"
    const val CATEGORY_NAME = "h3"
    const val LINK = "href"
}
