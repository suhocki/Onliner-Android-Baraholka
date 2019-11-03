package kt.school.starlord.model.data.mapper.converter

import kt.school.starlord.domain.data.mapper.BaseConverter
import kt.school.starlord.domain.entity.global.LocalizedTimePassed
import kt.school.starlord.domain.entity.product.LastUpdate
import kt.school.starlord.domain.entity.product.Price
import kt.school.starlord.domain.entity.product.Product
import kt.school.starlord.domain.entity.product.ProductOwner
import kt.school.starlord.domain.entity.product.ProductType
import kt.school.starlord.model.data.mapper.converter.DocumentToCategoriesWithSubcategoriesConverter.Companion.LINK
import kt.school.starlord.model.data.mapper.converter.localized.LocalizedTimePassedToEpochMilliConverter
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

/**
 * Contains logic on how to convert Jsoup Element to Product entity.
 */
class ElementToProductConverter(
    private val localizedTimePassedToEpochMilliConverter: LocalizedTimePassedToEpochMilliConverter
) : BaseConverter<Element, Product>(
    Element::class.java,
    Product::class.java
) {

    override fun convert(value: Element): Product {
        val (signatures, cost, title) = extractDocumentData(value)
        val description = value.getElementsByClass(Tags.DESCRIPTION)
        val comments = value.getElementsByClass(Tags.COMMENTS)
        val signature = signatures.first()
        val localizedTimePassed = LocalizedTimePassed(
            value.getElementsByClass(Tags.LAST_UPDATE).first().text()
                .replaceFirst(Tags.UP, "", true)
                .replaceIndent()
        )
        val epochMilli = localizedTimePassedToEpochMilliConverter.convert(localizedTimePassed)

        return Product(
            id = title.first().getElementsByTag(Tags.A).attr(LINK).split("=").last().toLong(),
            title = title.text(),
            description = if (description.isNotEmpty()) description.text() else "",
            image = value.getElementsByClass(Tags.IMAGE)
                .first()
                .getElementsByTag(Tags.IMG)
                .first()
                .attr(Tags.SRC),
            price = getProductPrice(cost.first()),
            location = if (signature.hasText()) signature.getElementsByTag(Tags.STRONG).first().text() else "",
            commentsCount = if (comments.isNotEmpty()) comments.text().toLong() else 0L,
            type = getProductType(value.getElementsByClass(ProductDocumentType.TYPE)),
            owner = getProductOwner(signature),
            isPaid = value.hasClass(Tags.M_IMP),
            lastUpdate = LastUpdate(epochMilli, localizedTimePassed)
        )
    }

    private fun extractDocumentData(element: Element) = ProductElements(
        signature = element.getElementsByClass(Tags.SIGNATURE),
        cost = element.getElementsByClass(Tags.COST),
        title = element.getElementsByClass(Tags.TITLE)
    )

    private fun getProductOwner(signature: Element): ProductOwner {
        val owner = signature.getElementsByTag(Tags.A).first()
        return ProductOwner(
            id = owner.attr(LINK).split(Tags.SEPARATOR).last().toLong(),
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

    private fun getProductPrice(cost: Element): Price {
        val amount = if (cost.hasText()) {
            cost.getElementsByClass(Tags.PRICE).text()
                .replace(",", ".")
                .replace(Tags.REGEX_ONLY_NUMBERS_AND_DOTS, "")
                .trimEnd('.')
                .toDoubleOrNull()
        } else {
            null
        }
        val isBargainAvailable =
            amount?.let { cost.getElementsByClass(Tags.COST_TORG).hasText() } ?: false
        return Price(amount, isBargainAvailable)
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

    private object Tags {
        const val A = "a"
        const val COST = "cost"
        const val COST_TORG = "cost-torg"
        const val TITLE = "wraptxt"
        const val DESCRIPTION = "ba-description"
        const val PRICE = "price-primary"
        const val IMAGE = "img-va"
        const val SIGNATURE = "ba-signature"
        const val COMMENTS = "c-org"
        const val IMG = "img"
        const val SRC = "src"
        const val STRONG = "strong"
        const val LAST_UPDATE = "ba-post-up"
        const val SEPARATOR = "user/"
        const val M_IMP = "m-imp"

        const val UP = "UP!"
        val REGEX_ONLY_NUMBERS_AND_DOTS = "[^\\d|.]".toRegex()
    }
}
