package kt.school.starlord.model.data.mapper.converter

import kt.school.starlord.entity.ProductOwner
import kt.school.starlord.entity.product.Product
import kt.school.starlord.entity.product.ProductPrice
import kt.school.starlord.entity.product.ProductType
import kt.school.starlord.entity.product.ProductsList
import kt.school.starlord.model.data.mapper.converter.DocumentToCategoriesWithSubcategoriesConverter.Companion.LINK
import kt.school.starlord.model.data.mapper.entity.BaseConverter
import org.jsoup.nodes.Document

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
                    val cost = product.getElementsByClass("cost").first()
                    if (title.hasText() && signature != null && cost != null) add(
                        Product(
                            id = title.first().getElementsByTag("a").attr(LINK).split("=").last().toLong(),
                            title = title.text(),
                            description = if (product.getElementsByClass(DESCRIPTION).isNotEmpty()) {
                                product.getElementsByClass(DESCRIPTION).text()
                            } else {
                                ""
                            },
                            image = product.getElementsByClass(IMAGE)
                                .first()
                                .getElementsByTag("img")
                                .first()
                                .attr("src"),
                            price = ProductPrice(
                                amount = cost.let { c ->
                                    if (c.hasText()) {
                                        c.getElementsByClass(PRICE).text()
                                            .replace(",", ".")
                                            .split(" ")[0]
                                            .toDoubleOrNull()
                                    } else {
                                        null
                                    }
                                },
                                isBargainAvailable = cost.getElementsByClass("cost-torg").hasText()
                            ),
                            location = if (signature.hasText()) {
                                signature.getElementsByTag("strong").first().text()
                            } else {
                                ""
                            },
                            lastUpdate = product.getElementsByClass("ba-post-up").first().text(),
                            commentsCount = if (product.getElementsByClass(COMMENTS).isNotEmpty()) {
                                product.getElementsByClass(COMMENTS).text().toLong()
                            } else {
                                0L
                            },
                            type = TODO(),
                            owner = signature.let {
                                val owner = it.getElementsByTag("a").first()
                                ProductOwner(
                                    id = owner.attr(LINK).split("user/").last().toLong(),
                                    name = owner.text()
                                )
                            },
                            isPaid = product.hasClass("m-imp")
                        )
                    )
                }
            }
        }.toList()
    )

    companion object {
        const val TABLE = "ba-tbl-list__table"
        const val TR = "tr"
        const val TITLE = "wraptxt"
        const val DESCRIPTION = "ba-description"
        const val PRICE = "price-primary"
        const val IMAGE = "img-va"
        const val SIGNATURE = "ba-signature"
        const val SELECT = "select"
        const val GRAY_CLASS = "gray"
        const val COMMENTS = "c-org"
    }
}

