package kt.school.starlord.model.data.mapper.converter

import kt.school.starlord.domain.data.mapper.BaseConverter
import kt.school.starlord.domain.entity.subcategory.Subcategory
import org.jsoup.nodes.Element

/**
 * Contains logic on how to convert Jsoup Element to Subcategory entity.
 */
class ElementToSubcategoryConverter : BaseConverter<Element, Subcategory>(
    Element::class.java,
    Subcategory::class.java
) {

    override fun convert(value: Element): Subcategory {
        val subcategoryElements = value.getElementsByAttribute(LINK)

        return Subcategory(
            name = subcategoryElements.text(),
            count = value.select(PRODUCTS_COUNT).text().toInt(),
            link = subcategoryElements.attr(LINK)
        )
    }

    companion object {
        private const val PRODUCTS_COUNT = "sup"
        private const val LINK = "href"
    }
}
