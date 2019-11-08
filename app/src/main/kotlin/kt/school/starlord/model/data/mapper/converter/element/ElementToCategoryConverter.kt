package kt.school.starlord.model.data.mapper.converter.element

import kt.school.starlord.domain.data.mapper.BaseConverter
import kt.school.starlord.domain.entity.category.Category
import org.jsoup.nodes.Element

/**
 * Contains logic on how to convert Jsoup Element to Category entity.
 */
class ElementToCategoryConverter : BaseConverter<Element, Category>(Element::class, Category::class) {

    override fun convert(value: Element) = Category(value.select(CATEGORY_NAME).text())

    companion object {
        const val CATEGORY_NAME = "h3"
    }
}
