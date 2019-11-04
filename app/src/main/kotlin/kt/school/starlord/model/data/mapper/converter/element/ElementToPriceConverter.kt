package kt.school.starlord.model.data.mapper.converter.element

import kt.school.starlord.domain.data.mapper.BaseConverter
import kt.school.starlord.domain.entity.product.Price
import org.jsoup.nodes.Element

/**
 * Contains logic on how to convert Jsoup Element to Price entity.
 */
class ElementToPriceConverter : BaseConverter<Element, Price>(Element::class.java, Price::class.java) {

    override fun convert(value: Element): Price {
        var hasPrice = value.hasText()
        var amount = 0.0
        var isBargainAvailable = false

        if (hasPrice) {
            value.getElementsByClass(PRICE).text()
                .replace(COMMA, DOT)
                .replace(REGEX_ONLY_NUMBERS_AND_DOTS, EMPTY_STRING)
                .trimEnd(DOT.first())
                .toDoubleOrNull()
                ?.let {
                    amount = it
                    isBargainAvailable = value.getElementsByClass(COST_TORG).hasText()
                } ?: let { hasPrice = false }
        }

        return Price(amount, hasPrice, isBargainAvailable)
    }

    companion object {
        private val REGEX_ONLY_NUMBERS_AND_DOTS = "[^\\d|.]".toRegex()

        private const val EMPTY_STRING = ""
        private const val COMMA = ","
        private const val DOT = "."
        private const val COST_TORG = "cost-torg"
        private const val PRICE = "price-primary"
    }
}
