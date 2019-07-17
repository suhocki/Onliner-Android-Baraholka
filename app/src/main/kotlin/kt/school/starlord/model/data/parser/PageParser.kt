package kt.school.starlord.model.data.parser

import kt.school.starlord.entity.Category
import kt.school.starlord.entity.Subcategory
import kt.school.starlord.model.data.mapper.Mapper

/**
 * Created for HTML parsing.
 */
class PageParser(private val mapper: Mapper) {

    companion object {
        private val categoriesRegex = """<h3>(.|\n)*?</div>""".toRegex()
        private val subcategoriesRegex = """<li>(.|\n)*?</li>""".toRegex()
    }

    /**
     * Parses categories with subcategories.
     *
     * @param page HTML page with categories and subcategories to parse
     * @return categories with appropriate subcategories.
     */
    fun parseCategories(page: String): Map<Category, List<Subcategory>> {
        return categoriesRegex.findAll(page)
            .map { categoryMatchResult ->

                val category = mapper.map<Category>(categoryMatchResult)

                val subcategories = subcategoriesRegex.findAll(categoryMatchResult.value)
                    .map { subcategoryMatchResult ->
                        mapper.map<Subcategory>(subcategoryMatchResult).apply { categoryName = category.name }
                    }
                    .toList()

                category to subcategories
            }
            .toMap()
    }
}
