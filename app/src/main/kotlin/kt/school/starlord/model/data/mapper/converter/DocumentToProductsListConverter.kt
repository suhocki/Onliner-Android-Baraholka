package kt.school.starlord.model.data.mapper.converter

import kt.school.starlord.entity.product.Product
import kt.school.starlord.entity.product.ProductOwner
import kt.school.starlord.entity.product.ProductPrice
import kt.school.starlord.entity.product.ProductType
import kt.school.starlord.entity.product.ProductsList
import kt.school.starlord.model.data.mapper.converter.DocumentToCategoriesWithSubcategoriesConverter.Companion.LINK
import kt.school.starlord.model.data.mapper.entity.BaseConverter
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

/**
 * Contains logic on how to convert Jsoup Document to CategoriesWithSubcategories entity.
 */
class DocumentToProductsListConverter : BaseConverter<Document, ProductsList>(
    Document::class.java, ProductsList::class.java
) {
    override fun convert(value: Document): ProductsList = ProductsList(
        value.getElementsByClass(Tags.TABLE)
            .flatMap { it.getElementsByTag(Tags.TR) }
            .filter(::isSuitableElement)
            .map(::createProduct)
            .toList()
    )

    private fun createProduct(element: Element): Product {
        val (signatures, cost, title) = extractDocumentData(element)
        val description = element.getElementsByClass(Tags.DESCRIPTION)
        val comments = element.getElementsByClass(Tags.COMMENTS)
        val signature = signatures.first()
        return Product(
            id = title.first().getElementsByTag(Tags.A).attr(LINK).split("=").last().toLong(),
            title = title.text(),
            description = if (description.isNotEmpty()) description.text() else "",
            image = element.getElementsByClass(Tags.IMAGE)
                .first()
                .getElementsByTag(Tags.IMG)
                .first()
                .attr(Tags.SRC),
            price = getProductPrice(cost.first()),
            location = if (signature.hasText()) signature.getElementsByTag(Tags.STRONG).first().text() else "",
            commentsCount = if (comments.isNotEmpty()) comments.text().toLong() else 0L,
            type = getProductType(element.getElementsByClass(ProductDocumentType.TYPE)),
            owner = getProductOwner(signature),
            isPaid = element.hasClass(Tags.M_IMP),
            lastUpdate = element.getElementsByClass(Tags.LAST_UPDATE).first().text()
                .replaceFirst(Tags.UP, "", true)
                .replaceIndent()
        )
    }

    private fun isSuitableElement(element: Element): Boolean {
        val (signature, cost, title) = extractDocumentData(element)
        return title.hasText() && signature.first() != null && cost.first() != null
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

    private fun getProductPrice(cost: Element): ProductPrice {
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
        return ProductPrice(amount, isBargainAvailable)
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
        const val TABLE = "ba-tbl-list__table"
        const val TR = "tr"
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
