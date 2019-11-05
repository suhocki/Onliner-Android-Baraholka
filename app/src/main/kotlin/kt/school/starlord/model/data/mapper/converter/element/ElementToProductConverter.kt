package kt.school.starlord.model.data.mapper.converter.element

import kt.school.starlord.domain.data.mapper.BaseConverter
import kt.school.starlord.domain.data.mapper.Mapper
import kt.school.starlord.domain.entity.global.LocalizedTimePassed
import kt.school.starlord.domain.entity.product.Product
import kt.school.starlord.domain.entity.product.ProductOwner
import kt.school.starlord.domain.entity.product.ProductType
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * Contains logic on how to convert Jsoup Element to Product entity.
 */
class ElementToProductConverter : BaseConverter<Element, Product>(
    Element::class.java,
    Product::class.java
), KoinComponent {

    private val mapper: Mapper by inject()

    override fun convert(value: Element): Product {
        val (signatures, cost, title) = extractDocumentData(value)
        val description = value.getElementsByClass(DESCRIPTION)
        val comments = value.getElementsByClass(COMMENTS)
        val signature = signatures.first()
        val localizedTimePassed = LocalizedTimePassed(
            value.getElementsByClass(LAST_UPDATE).first().text()
                .replaceFirst(UP, "", true)
                .replaceIndent()
        )

        return Product(
            id = title.first().getElementsByTag(A).attr(LINK).split("=").last().toLong(),
            title = title.text(),
            description = if (description.isNotEmpty()) description.text() else "",
            image = value.getElementsByClass(IMAGE)
                .first()
                .getElementsByTag(IMG)
                .first()
                .attr(SRC),
            price = mapper.map(cost.first()),
            location = if (signature.hasText()) signature.getElementsByTag(STRONG).first().text() else "",
            commentsCount = if (comments.isNotEmpty()) comments.text().toLong() else 0L,
            type = getProductType(value.getElementsByClass(ProductDocumentType.TYPE)),
            owner = getProductOwner(signature),
            isPaid = value.hasClass(M_IMP),
            epochMilli = mapper.map(localizedTimePassed),
            localizedTimePassed = localizedTimePassed
        )
    }

    private fun extractDocumentData(element: Element) =
        ProductElements(
            signature = element.getElementsByClass(SIGNATURE),
            cost = element.getElementsByClass(COST),
            title = element.getElementsByClass(TITLE)
        )

    private fun getProductOwner(signature: Element): ProductOwner {
        val owner = signature.getElementsByTag(A).first()
        return ProductOwner(
            id = owner.attr(LINK).split(
                SEPARATOR
            ).last().toLong(),
            name = owner.text()
        )
    }

    private fun getProductType(elements: Elements): ProductType {
        return when {
            elements.hasClass(ProductDocumentType.SELL) -> ProductType.SELL
            elements.hasClass(ProductDocumentType.BUY) -> ProductType.BUY
            elements.hasClass(ProductDocumentType.RENT) -> ProductType.RENT
            elements.hasClass(ProductDocumentType.EXCHANGE) -> ProductType.EXCHANGE
            elements.hasClass(ProductDocumentType.SERVICE) -> ProductType.SERVICE
            elements.hasClass(ProductDocumentType.CLOSED) -> ProductType.CLOSED
            elements.hasClass(ProductDocumentType.WARNING) -> ProductType.WARNING
            else -> error("Unknown product type.")
        }
    }

    private data class ProductElements(
        val signature: Elements,
        val cost: Elements,
        val title: Elements
    )

    private object ProductDocumentType {
        const val TYPE = "ba-label"
        const val WARNING = "ba-label-1"
        const val SELL = "ba-label-2"
        const val BUY = "ba-label-3"
        const val EXCHANGE = "ba-label-4"
        const val SERVICE = "ba-label-5"
        const val RENT = "ba-label-6"
        const val CLOSED = "ba-label-7"
    }

    companion object {
        private const val LINK = "href"
        private const val A = "a"
        private const val COST = "cost"
        private const val TITLE = "wraptxt"
        private const val DESCRIPTION = "ba-description"
        private const val IMAGE = "img-va"
        private const val SIGNATURE = "ba-signature"
        private const val COMMENTS = "c-org"
        private const val IMG = "img"
        private const val SRC = "src"
        private const val STRONG = "strong"
        private const val LAST_UPDATE = "ba-post-up"
        private const val SEPARATOR = "user/"
        private const val M_IMP = "m-imp"
        private const val UP = "UP!"
    }
}
