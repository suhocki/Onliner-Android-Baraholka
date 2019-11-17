package kt.school.starlord.model.data.mapper.converter.element

import kt.school.starlord.domain.entity.subcategory.Subcategory
import org.jsoup.Jsoup
import org.jsoup.parser.Parser
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory

internal class ElementToSubcategoryConverterTest {

    private val converter = ElementToSubcategoryConverter()

    @TestFactory
    fun convertElements() = mapOf(
        createElement(
            "<li> <a href=\"./viewforum.php?f=2\">Мобильные телефоны</a> <sup> 6947 </sup> </li>"
        ) to Subcategory("Мобильные телефоны", 6947, "./viewforum.php?f=2"),
        createElement(
            "<li> <a href=\"./viewforum.php?f=38\">Мобильные телефоны: аксессуары и запчасти</a> <sup> 7010 </sup> </li>"
        ) to Subcategory("Мобильные телефоны: аксессуары и запчасти", 7010, "./viewforum.php?f=38"),
        createElement(
            "<li> <a href=\"./viewforum.php?f=180\">Сборка. Услуги. Сервис. Готовые компьютеры и серверы</a> <sup> 1674 </sup> </li>"
        ) to Subcategory("Сборка. Услуги. Сервис. Готовые компьютеры и серверы", 1674, "./viewforum.php?f=180")
    ).map { (input, expected) ->
        DynamicTest.dynamicTest("Converting HTML to Subcategory: ${expected.name}") {
            val actual = converter.convert(input)
            Assertions.assertEquals(expected, actual)
        }
    }

    private fun createElement(htmlPart: String) =
        Jsoup.parse(htmlPart, "", Parser.xmlParser()).select("li").first()
}