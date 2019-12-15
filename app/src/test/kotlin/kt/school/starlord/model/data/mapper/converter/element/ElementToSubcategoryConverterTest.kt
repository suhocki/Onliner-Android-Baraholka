package kt.school.starlord.model.data.mapper.converter.element

import androidx.test.ext.junit.runners.AndroidJUnit4
import kt.school.starlord.domain.entity.subcategory.Subcategory
import org.jsoup.Jsoup
import org.jsoup.parser.Parser
import org.junit.Test
import org.junit.jupiter.api.Assertions
import org.junit.runner.RunWith
import org.koin.test.AutoCloseKoinTest

/* ktlint-disable */
@RunWith(AndroidJUnit4::class)
internal class ElementToSubcategoryConverterTest : AutoCloseKoinTest() {

    @Test
    fun convertElements() {
        //given
        val htmlPart = "<li> <a href=\"./viewforum.php?f=2\">Мобильные телефоны</a> <sup> 6947 </sup> </li>"
        val element = Jsoup.parse(htmlPart, "", Parser.xmlParser()).select("li").first()

        //when
        val actual = ElementToSubcategoryConverter().convert(element)

        //then
        Assertions.assertEquals(Subcategory("Мобильные телефоны", 6947, 2L), actual)
    }
}
