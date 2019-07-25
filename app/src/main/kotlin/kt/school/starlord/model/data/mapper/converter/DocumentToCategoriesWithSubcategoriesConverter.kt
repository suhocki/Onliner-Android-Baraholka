package kt.school.starlord.model.data.mapper.converter

import kt.school.starlord.entity.CategoriesWithSubcategories
import kt.school.starlord.entity.Category
import kt.school.starlord.entity.Subcategory
import kt.school.starlord.model.data.mapper.entity.BaseConverter
import org.jsoup.nodes.Document

/**
 * Contains logic on how to convert Jsoup Document to CategoriesWithSubcategories entity.
 */
class DocumentToCategoriesWithSubcategoriesConverter : BaseConverter<Document, CategoriesWithSubcategories>(
    Document::class.java, CategoriesWithSubcategories::class.java
) {
    override fun convert(value: Document): CategoriesWithSubcategories {
        val htmlCategories = value.getElementsByClass(CATEGORIES)
        val categories = mutableListOf<Category>()
        val subcategories = mutableListOf<Subcategory>()

        htmlCategories.forEach {
            val categoryName = it.select(CATEGORY_NAME).text()

            val htmlSubcategories = it.getElementsByClass(SUBCATEGORIES)
                .first()
                .select(SUBCATEGORIES_DATA)

            val category = Category(categoryName)

            val subcategoriesOfCategory = htmlSubcategories.map { htmlSubcategory ->
                val htmlSubcategoryNameAndLink = htmlSubcategory.getElementsByAttribute(LINK)
                Subcategory(
                    name = htmlSubcategoryNameAndLink.text(),
                    categoryName = category.name,
                    count = htmlSubcategory.select(PRODUCTS_COUNT).text().toInt(),
                    link = htmlSubcategoryNameAndLink.attr(LINK)
                )
            }

            categories.add(category)
            subcategories.addAll(subcategoriesOfCategory)
        }

        return CategoriesWithSubcategories(categories, subcategories)
    }

    companion object {
        const val CATEGORIES = "cm-onecat"
        const val SUBCATEGORIES = "b-cm-list"
        const val PRODUCTS_COUNT = "sup"
        const val SUBCATEGORIES_DATA = "li"
        const val CATEGORY_NAME = "h3"
        const val LINK = "href"
    }
}
