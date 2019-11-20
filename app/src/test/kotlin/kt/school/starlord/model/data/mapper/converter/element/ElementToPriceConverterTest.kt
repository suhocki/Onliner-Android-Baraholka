package kt.school.starlord.model.data.mapper.converter.element

import kt.school.starlord.domain.entity.product.Price
import org.jsoup.Jsoup
import org.jsoup.parser.Parser
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory

/* ktlint-disable */
internal class ElementToPriceConverterTest {

    private val converter = ElementToPriceConverter()

    @TestFactory
    fun convertElements() = mapOf(
        createElement(
            "<td class=\"cost\"> </td>"
        ) to Price(
            amount = 0.0,
            hasPrice = false,
            isBargainAvailable = false
        ),
        createElement(
            "<td class=\"cost\"> <div class=\"price-primary\"> 430,00 р. </div> <small class=\"cost-torg\"> </small> </td>"
        ) to Price(
            amount = 430.0,
            hasPrice = true,
            isBargainAvailable = false
        ),
        createElement(
            "<td class=\"cost\"> <div class=\"price-primary\"> 160,00 р. </div> <small class=\"cost-torg\"> торг </small> </td>"
        ) to Price(
            amount = 160.0,
            hasPrice = true,
            isBargainAvailable = true
        )
    ).map { (input, expected) ->
        DynamicTest.dynamicTest("Converting HTML to Price") {
            val actual = converter.convert(input)
            Assertions.assertEquals(expected, actual)
        }
    }

    private fun createElement(htmlPart: String) =
        Jsoup.parse(htmlPart, "", Parser.xmlParser()).select("td").first()
}
