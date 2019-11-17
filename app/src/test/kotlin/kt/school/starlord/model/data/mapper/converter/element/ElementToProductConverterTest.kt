package kt.school.starlord.model.data.mapper.converter.element

import io.mockk.mockk
import kt.school.starlord.domain.data.mapper.BaseConverter
import kt.school.starlord.domain.data.mapper.Mapper
import kt.school.starlord.domain.entity.product.Price
import kt.school.starlord.domain.entity.product.Product
import kt.school.starlord.domain.entity.product.ProductOwner
import kt.school.starlord.domain.entity.product.ProductType
import kt.school.starlord.ui.global.entity.wrapper.LocalizedTimePassed
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.parser.Parser
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.mockito.ArgumentMatchers

internal class ElementToProductConverterTest : KoinTest {

    private val converter by lazy { ElementToProductConverter() }

    private val price: Price = mockk()
    private val epochMillis = ArgumentMatchers.anyLong()

    private val converters = setOf(
        object : BaseConverter<Element, Price>(Element::class, Price::class) {
            override fun convert(value: Element) = price
        },
        object : BaseConverter<LocalizedTimePassed, Long>(LocalizedTimePassed::class, Long::class) {
            override fun convert(value: LocalizedTimePassed) = epochMillis
        })

    @BeforeEach
    fun setUp() {
        startKoin {
            modules(module { single { Mapper(converters) } })
        }
    }

    @AfterEach
    fun afterEach() {
        stopKoin()
    }

    @TestFactory
    fun convertElements() = mapOf(
        createElement(
            "<tr class=\"m-imp\"> <td class=\"frst ph colspan\" colspan=\"2\"> <table width=\"100%\"> <tbody> <tr> <td class=\"ph\"> <div class=\"ph-i\"> <span class=\"img-va\"><a href=\"./viewtopic.php?t=23252834\"><img src=\"https://content.onliner.by/baraholka/icon_large/23252834\"></a><i class=\"va\"></i></span> </div> </td> <td class=\"txt\"> <div class=\"txt-i\"> <div class=\"ba-label ba-label-1\"></div> <table style=\"table-layout:fixed; width:100%; overflow: hidden\"> <tbody> <tr> <td style=\"padding:0; border-width:0\"> <h2 class=\"wraptxt\"><a href=\"./viewtopic.php?t=23252834\">Мобильные телефоны от пользователей в Каталоге Onliner</a></h2> <p class=\"ba-description\">привычный вид Барахолки с возможностью сортировок и фильтрации</p> </td> </tr> </tbody> </table> <p class=\"ba-signature\"> <strong>Минск</strong> <a class=\"gray\" href=\"https://profile.onliner.by/user/8250\">Onliner.by</a> </p> </div> </td> </tr> </tbody> </table> </td> <td class=\"cost\"> </td> <td class=\"lst post-tls\"> <div class=\"ba-topic-last-post-data\" style=\"display:none\"> Onliner.by <br>более 1 месяца назад </div> <p class=\"ba-post-coms\"> &nbsp; </p> <p class=\"ba-post-up\"><small class=\"tot-up\">UP!</small> более 1 месяца назад</p> </td> </tr>"
        ) to Product(
            id = 23252834,
            location = "Минск",
            title = "Мобильные телефоны от пользователей в Каталоге Onliner",
            description = "привычный вид Барахолки с возможностью сортировок и фильтрации",
            image = "https://content.onliner.by/baraholka/icon_large/23252834",
            commentsCount = 0,
            isPaid = true,
            owner = ProductOwner("Onliner.by", 8250),
            type = ProductType.WARNING,
            localizedTimePassed = LocalizedTimePassed("более 1 месяца назад"),
            price = price,
            lastUpdate = epochMillis
        ),
        createElement(
            "<tr class=\"m-imp\"> <td class=\"frst ph colspan\" colspan=\"2\"> <table width=\"100%\"> <tbody> <tr> <td class=\"ph\"> <div class=\"ph-i\"> <span class=\"img-va\"><a href=\"./viewtopic.php?t=20088700\"><img src=\"https://content.onliner.by/baraholka/icon_large/20088700\"></a><i class=\"va\"></i></span> </div> </td> <td class=\"txt\"> <div class=\"txt-i\"> <div class=\"ba-label ba-label-2\"></div> <table style=\"table-layout:fixed; width:100%; overflow: hidden\"> <tbody> <tr> <td style=\"padding:0; border-width:0\"> <h2 class=\"wraptxt\"><a href=\"./viewtopic.php?t=20088700\">iPhone 11/11 PRO/11 PRO MAX/XS/XS MAX/XR/X(10)</a></h2> <p class=\"ba-description\">100% ОРИГИНАЛ* Гарантийный Сертификат Официального Сервиса до 36 месяцев. Вся техника новая, запечатанная и не активированная ! 30 дней на проверку ! Возможен безналичный расчёт. В подарок портативная аудио система ! +375(29)6700266 viber</p> </td> </tr> </tbody> </table> <p class=\"ba-signature\"> <strong>Минск</strong> <span class=\"baraholka-list__delivery\"> Доставка по Беларуси </span> <a class=\"gray\" href=\"https://profile.onliner.by/user/2565106\">i-product</a> <span class=\"ba-pgs\"><a href=\"./viewtopic.php?t=20088700&amp;start=0\">1</a> ... <a href=\"./viewtopic.php?t=20088700&amp;start=340\">18</a>&nbsp;<a href=\"./viewtopic.php?t=20088700&amp;start=360\">19</a>&nbsp;<a href=\"./viewtopic.php?t=20088700&amp;start=380\">20</a></span> </p> </div> </td> </tr> </tbody> </table> </td> <td class=\"cost\"> <div class=\"price-primary\"> 1680,00 р. </div> <small class=\"cost-torg\"> торг </small> </td> <td class=\"lst post-tls\"> <div class=\"ba-topic-last-post-data\" style=\"display:none\"> i-product <br>9 дней назад </div> <p class=\"ba-post-coms\"> <a href=\"./viewtopic.php?t=20088700&amp;view=unread#unread\" alt=\"i-product, 9 дней назад\" title=\"i-product, 9 дней назад\" class=\"b-ico i-sm-postnew\"></a> <a href=\"./viewtopic.php?t=20088700&amp;view=unread#unread\" alt=\"i-product, 9 дней назад\" title=\"i-product, 9 дней назад\" class=\"c-org\"> 396</a> </p> <p class=\"ba-post-up\"><small class=\"tot-up\">UP!</small> 44 минуты назад</p> </td> </tr>"
        ) to Product(
            id = 20088700,
            location = "Минск",
            title = "iPhone 11/11 PRO/11 PRO MAX/XS/XS MAX/XR/X(10)",
            description = "100% ОРИГИНАЛ* Гарантийный Сертификат Официального Сервиса до 36 месяцев. Вся техника новая, запечатанная и не активированная ! 30 дней на проверку ! Возможен безналичный расчёт. В подарок портативная аудио система ! +375(29)6700266 viber",
            image = "https://content.onliner.by/baraholka/icon_large/20088700",
            commentsCount = 396,
            isPaid = true,
            owner = ProductOwner("i-product", 2565106),
            type = ProductType.SELL,
            localizedTimePassed = LocalizedTimePassed("44 минуты назад"),
            price = price,
            lastUpdate = epochMillis
        ),
        createElement(
            "<tr> <td class=\"frst ph colspan\" colspan=\"2\"> <table width=\"100%\"> <tbody> <tr> <td class=\"ph\"> <div class=\"ph-i\"> <span class=\"img-va\"><a href=\"./viewtopic.php?t=21506479\"><img src=\"https://content.onliner.by/baraholka/icon/21506479\"></a><i class=\"va\"></i></span> </div> </td> <td class=\"txt\"> <div class=\"txt-i\"> <div class=\"ba-label ba-label-2\"></div> <table style=\"table-layout:fixed; width:100%; overflow: hidden\"> <tbody> <tr> <td style=\"padding:0; border-width:0\"> <h2 class=\"wraptxt\"><a href=\"./viewtopic.php?t=21506479\">Шкафы-купе, корпусная и встроенная мебель, кухни на заказ</a></h2> <p class=\"ba-description\">Шкафы-купе в современных системах \"под ключ\". +375293130893. ИП Юхновский ДВ. УНП 191870024</p> </td> </tr> </tbody> </table> <p class=\"ba-signature\"> <strong>Минск</strong> <a class=\"gray\" href=\"https://profile.onliner.by/user/152656\">dzeniska82</a> </p> </div> </td> </tr> </tbody> </table> </td> <td class=\"cost\"> <div class=\"price-primary\"> 1,00 р. </div> <small class=\"cost-torg\"> торг </small> </td> <td class=\"lst post-tls\"> <div class=\"ba-topic-last-post-data\" style=\"display:none\"> new_saler <br>6 дней назад </div> <p class=\"ba-post-coms\"> <a href=\"./viewtopic.php?t=21506479&amp;view=unread#unread\" alt=\"new_saler, 6 дней назад\" title=\"new_saler, 6 дней назад\" class=\"b-ico i-sm-postnew\"></a> <a href=\"./viewtopic.php?t=21506479&amp;view=unread#unread\" alt=\"new_saler, 6 дней назад\" title=\"new_saler, 6 дней назад\" class=\"c-org\"> 11</a> </p> <p class=\"ba-post-up\"><small class=\"tot-up\">UP!</small> 12 минут назад</p> </td> </tr>"
        ) to Product(
            id = 21506479,
            location = "Минск",
            title = "Шкафы-купе, корпусная и встроенная мебель, кухни на заказ",
            description = "Шкафы-купе в современных системах \"под ключ\". +375293130893. ИП Юхновский ДВ. УНП 191870024",
            image = "https://content.onliner.by/baraholka/icon/21506479",
            commentsCount = 11,
            isPaid = false,
            owner = ProductOwner("dzeniska82", 152656),
            type = ProductType.SELL,
            localizedTimePassed = LocalizedTimePassed("12 минут назад"),
            price = price,
            lastUpdate = epochMillis
        )
    ).map { (input, expected) ->
        DynamicTest.dynamicTest("Converting HTML to Product, id = ${expected.id}") {
            val actual = converter.convert(input)
            Assertions.assertEquals(expected, actual)
        }
    }

    private fun createElement(htmlPart: String) =
        Jsoup.parse(htmlPart, "", Parser.xmlParser()).select("tr").first()

}
