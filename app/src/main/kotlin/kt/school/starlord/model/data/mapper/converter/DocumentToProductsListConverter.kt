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
 * Contains logic on how to convert Jsoup Docu  ment to CategoriesWithSubcategories entity.
 */
class DocumentToProductsListConverter : BaseConverter<Document, ProductsList>(
    Document::class.java, ProductsList::class.java
) {
    override fun convert(value: Document): ProductsList = ProductsList(
        HashSet<Product>().apply {
            value.getElementsByClass(TABLE).forEach { table ->
                table.getElementsByTag(TR).forEach { product ->
                    val signature = product.getElementsByClass(SIGNATURE).first()
                    val title = product.getElementsByClass(TITLE)
                    val cost = product.getElementsByClass(COST).first()
                    if (title.hasText() && signature != null && cost != null) add(
                        Product(
                            id = getId(title),
                            title = title.text(),
                            description = getDescription(product),
                            image = getImgLink(product),
                            price = getProductPrice(cost),
                            location = getLocation(signature),
                            commentsCount = getCommentsCount(product),
                            type = getProductType(product),
                            owner = getProductOwner(signature),
                            isPaid = product.hasClass(M_IMP),
                            lastUpdate = product.getElementsByClass(LAST_UPDATE).first().text()
                                .replaceFirst(UP, "", true)
                                .replaceIndent()
                        )
                    )
                }
            }
        }.toList()
    )

    private fun getProductOwner(signature: Element) =
        signature.let {
            val owner = it.getElementsByTag(A).first()
            ProductOwner(
                id = owner.attr(LINK).split(SEPARATOR).last().toLong(),
                name = owner.text()
            )
        }

    private fun getProductType(product: Element) =
        product.getElementsByClass(TYPE).let {
            when {
                it.hasClass(SELL) -> ProductType.SELL
                it.hasClass(BUY) -> ProductType.BUY
                it.hasClass(RENT) -> ProductType.RENT
                it.hasClass(EXCHANGE) -> ProductType.EXCHANGE
                it.hasClass(SERVICE) -> ProductType.SERVICE
                it.hasClass(CLOSED) -> ProductType.CLOSED
                else -> error("Unknown product type.")
            }
        }

    private fun getCommentsCount(product: Element): Long =
        if (product.getElementsByClass(COMMENTS).isNotEmpty()) {
            product.getElementsByClass(COMMENTS).text().toLong()
        } else {
            0L
        }

    private fun getLocation(signature: Element): String =
        if (signature.hasText()) {
            signature.getElementsByTag(STRONG).first().text()
        } else {
            ""
        }

    private fun getProductPrice(cost: Element): ProductPrice {
        val amount = if (cost.hasText()) {
            cost.getElementsByClass(PRICE).text()
                .replace(",", ".")
                .split(" ")[0]
                .toDoubleOrNull()
        } else {
            null
        }
        val isBargainAvailable = amount?.let { cost.getElementsByClass(COST_TORG).hasText() } ?: false
        return ProductPrice(amount, isBargainAvailable)
    }

    private fun getImgLink(product: Element): String =
        product.getElementsByClass(IMAGE)
            .first()
            .getElementsByTag(IMG)
            .first()
            .attr(SRC)

    private fun getDescription(product: Element): String =
        if (product.getElementsByClass(DESCRIPTION).isNotEmpty()) {
            product.getElementsByClass(DESCRIPTION).text()
        } else {
            ""
        }

    private fun getId(title: Elements) =
        title.first().getElementsByTag(A).attr(LINK).split("=").last().toLong()

    companion object {
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
        const val TYPE = "ba-label"
        const val SELL = "ba-label-2"
        const val BUY = "ba-label-3"
        const val EXCHANGE = "ba-label-4"
        const val SERVICE = "ba-label-5"
        const val RENT = "ba-label-6"
        const val CLOSED = "ba-label-7"
        const val UP = "UP!"
    }
}
