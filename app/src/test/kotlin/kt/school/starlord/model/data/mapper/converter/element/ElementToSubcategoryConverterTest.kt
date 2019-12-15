package kt.school.starlord.model.data.mapper.converter.element

import io.mockk.every
import io.mockk.mockk
import kt.school.starlord.domain.entity.subcategory.Subcategory
import kt.school.starlord.model.data.android.UriManager
import kt.school.starlord.ui.AfterEachCloseKoinTest
import org.jsoup.Jsoup
import org.jsoup.parser.Parser
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest

/* ktlint-disable */
internal class ElementToSubcategoryConverterTest: AfterEachCloseKoinTest() {

    private val converter by lazy { ElementToSubcategoryConverter() }

    @BeforeEach
    fun setUp() {
        startKoin {
            modules(module { single { uriManager } })
        }

        every { uriManager.getSubcategoryId("./viewforum.php?f=2") } returns 2L
        every { uriManager.getSubcategoryId("./viewforum.php?f=38") } returns 38L
        every { uriManager.getSubcategoryId("./viewforum.php?f=180") } returns 180L
    }

    @TestFactory
    fun convertElements() = mapOf(
        createElement(
            "<li> <a href=\"./viewforum.php?f=2\">Мобильные телефоны</a> <sup> 6947 </sup> </li>"
        ) to Subcategory("Мобильные телефоны", 6947, 2L),
        createElement(
            "<li> <a href=\"./viewforum.php?f=38\">Мобильные телефоны: аксессуары и запчасти</a> <sup> 7010 </sup> </li>"
        ) to Subcategory("Мобильные телефоны: аксессуары и запчасти", 7010, 38L),
        createElement(
            "<li> <a href=\"./viewforum.php?f=180\">Сборка. Услуги. Сервис. Готовые компьютеры и серверы</a> <sup> 1674 </sup> </li>"
        ) to Subcategory("Сборка. Услуги. Сервис. Готовые компьютеры и серверы", 1674, 180L)
    ).map { (input, expected) ->
        DynamicTest.dynamicTest("Converting HTML to Subcategory: ${expected.name}") {
            val actual = converter.convert(input)
            Assertions.assertEquals(expected, actual)
        }
    }

    private fun createElement(htmlPart: String) =
        Jsoup.parse(htmlPart, "", Parser.xmlParser()).select("li").first()
}
