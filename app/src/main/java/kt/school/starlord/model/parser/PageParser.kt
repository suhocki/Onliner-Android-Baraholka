package kt.school.starlord.model.parser

import kt.school.starlord.entity.Category
import kt.school.starlord.entity.Subcategory
import kt.school.starlord.model.mapper.Mapper

/**
 * Created for HTML parsing
 */
class PageParser(private val mapper: Mapper) {

    companion object {
        private val CATEGORIES_REGEX = """<h3>(.|\n)*?</div>""".toRegex()
        private val SUBCATEGORIES_REGEX = """<li>(.|\n)*?</li>""".toRegex()
    }

    /**
     * Parses categories with subcategories
     *
     * @param page HTML page with categories and subcategories to parse
     * @return categories with appropriate subcategories.
     */
    fun parseCategories(page: String): Map<Category, List<Subcategory>> {
        return CATEGORIES_REGEX.findAll(page)
            .map { categoryMatchResult ->

                val category = mapper.map<Category>(categoryMatchResult)

                val subcategories = SUBCATEGORIES_REGEX.findAll(categoryMatchResult.value)
                    .map { subcategoryMatchResult ->
                        mapper.map<Subcategory>(subcategoryMatchResult).apply { categoryName = category.name }
                    }
                    .toList()

                category to subcategories
            }
            .toMap()
    }
}