package kt.school.starlord.model.data.mapper.converter.element

import android.net.Uri
import kt.school.starlord.domain.entity.subcategory.Subcategory
import kt.school.starlord.domain.mapper.BaseConverter
import org.jsoup.nodes.Element

/**
 * Contains logic on how to convert Jsoup Element to Subcategory entity.
 */
class ElementToSubcategoryConverter : BaseConverter<Element, Subcategory>(Element::class, Subcategory::class) {

    override fun convert(value: Element): Subcategory {
        val subcategoryElements = value.getElementsByAttribute(LINK)
        val forumUrlPart = subcategoryElements.attr(LINK)
        val id = Uri.parse(forumUrlPart)
            .getQueryParameter(FORUM_ID)
            ?.toLongOrNull()
            ?: error("Forum id does not exist.")

        return Subcategory(
            name = subcategoryElements.text(),
            count = value.select(PRODUCTS_COUNT).text().toIntOrNull(),
            id = id
        )
    }

    companion object {
        private const val PRODUCTS_COUNT = "sup"
        private const val LINK = "href"
        private const val FORUM_ID = "f"
    }
}
