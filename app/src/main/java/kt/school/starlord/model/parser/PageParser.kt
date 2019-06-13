package kt.school.starlord.model.parser

import kt.school.starlord.entity.Category
import kt.school.starlord.entity.Subcategory
import kt.school.starlord.model.mapper.Mapper

class PageParser(private val mapper: Mapper) {

    companion object {
        private val CATEGORIES_REGEX = """<h3>(.|\n)*?</div>""".toRegex()
        private val SUBCATEGORIES_REGEX = """<li>(.|\n)*?</li>""".toRegex()
    }

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