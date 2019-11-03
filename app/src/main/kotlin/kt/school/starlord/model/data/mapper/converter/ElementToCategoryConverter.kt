package kt.school.starlord.model.data.mapper.converter

import kt.school.starlord.domain.data.mapper.BaseConverter
import kt.school.starlord.domain.entity.category.Category
import kt.school.starlord.domain.entity.subcategory.Subcategory
import org.jsoup.nodes.Element

/**
 * Contains logic on how to convert Jsoup Element to Category entity.
 */
class ElementToCategoryConverter : BaseConverter<Element, Category>(Element::class.java, Category::class.java) {

    override fun convert(value: Element) = Category(value.select(CATEGORY_NAME).text())

    companion object {
        const val CATEGORY_NAME = "h3"
    }
}
