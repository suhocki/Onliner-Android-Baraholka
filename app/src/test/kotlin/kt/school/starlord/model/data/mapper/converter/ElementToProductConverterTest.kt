package kt.school.starlord.model.data.mapper.converter

import io.mockk.every
import io.mockk.mockkStatic
import kt.school.starlord.domain.entity.global.TimeUnit
import kt.school.starlord.domain.entity.global.Timestamp
import kt.school.starlord.domain.entity.product.Price
import kt.school.starlord.domain.entity.product.Product
import kt.school.starlord.domain.entity.product.ProductOwner
import kt.school.starlord.domain.entity.product.ProductType
import org.jsoup.nodes.Element
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import org.threeten.bp.Instant

class ElementToProductConverterTest {

    private val converter = ElementToProductConverter()
    private val now = Instant.now()
    private val millisNow = now.toEpochMilli()

    @BeforeEach
    fun setUp() {
        mockkStatic(Instant::class)

        every { Instant.now() } returns now
    }

    @TestFactory
    fun convertElements() = mapOf(
        Element("tr").html(
            "<tr class=\"m-imp\"> <td class=\"frst ph colspan\" colspan=\"2\"> <table width=\"100%\"> <tbody> <tr> <td class=\"ph\"> <div class=\"ph-i\"> <span class=\"img-va\"><a href=\"./viewtopic.php?t=23252834\"><img src=\"https://content.onliner.by/baraholka/icon_large/23252834\"></a><i class=\"va\"></i></span> </div> </td> <td class=\"txt\"> <div class=\"txt-i\"> <div class=\"ba-label ba-label-1\"></div> <table style=\"table-layout:fixed; width:100%; overflow: hidden\"> <tbody> <tr> <td style=\"padding:0; border-width:0\"> <h2 class=\"wraptxt\"><a href=\"./viewtopic.php?t=23252834\">Мобильные телефоны от пользователей в Каталоге Onliner</a></h2> <p class=\"ba-description\">привычный вид Барахолки с возможностью сортировок и фильтрации</p> </td> </tr> </tbody> </table> <p class=\"ba-signature\"> <strong>Минск</strong> <a class=\"gray\" href=\"https://profile.onliner.by/user/8250\">Onliner.by</a> </p> </div> </td> </tr> </tbody> </table> </td> <td class=\"cost\"> </td> <td class=\"lst post-tls\"> <div class=\"ba-topic-last-post-data\" style=\"display:none\"> Onliner.by <br>более 1 месяца назад </div> <p class=\"ba-post-coms\"> &nbsp; </p> <p class=\"ba-post-up\"><small class=\"tot-up\">UP!</small> более 1 месяца назад</p> </td> </tr>"
        ) to Product(
            id = 23252834,
            location = "Минск",
            title = "Мобильные телефоны от пользователей в Каталоге Onliner",
            description = "привычный вид Барахолки с возможностью сортировок и фильтрации",
            image = "https://content.onliner.by/baraholka/icon_large/23252834",
            timestamp = Timestamp(0, TimeUnit.MONTH),
            commentsCount = 0,
            isPaid = false,
            owner = ProductOwner("Onliner.by", 8250),
            price = Price(null, false),
            type = ProductType.WARNING
        ),
        Element("tr").html("""
            <tr class="m-imp">
 <td class="frst ph colspan" colspan="2">
  <table width="100%">
   <tbody>
    <tr>
     <td class="ph">
      <div class="ph-i">
       <span class="img-va"><a href="./viewtopic.php?t=20088700"><img src="https://content.onliner.by/baraholka/icon_large/20088700"></a><i class="va"></i></span>
      </div> </td>
     <td class="txt">
      <div class="txt-i">
       <div class="ba-label ba-label-2"></div>
       <table style="table-layout:fixed; width:100%; overflow: hidden">
        <tbody>
         <tr>
          <td style="padding:0; border-width:0"> <h2 class="wraptxt"><a href="./viewtopic.php?t=20088700">iPhone 11/11 PRO/11 PRO MAX/XS/XS MAX/XR/X(10)</a></h2> <p class="ba-description">100% ОРИГИНАЛ* Гарантийный Сертификат Официального Сервиса до 36 месяцев. Вся техника новая, запечатанная и не активированная ! 30 дней на проверку ! Возможен безналичный расчёт. В подарок портативная аудио система ! +375(29)6700266 viber</p> </td>
         </tr>
        </tbody>
       </table>
       <p class="ba-signature"> <strong>Минск</strong> <span class="baraholka-list__delivery"> Доставка по Беларуси </span> <a class="gray" href="https://profile.onliner.by/user/2565106">i-product</a> <span class="ba-pgs"><a href="./viewtopic.php?t=20088700&amp;start=0">1</a> ... <a href="./viewtopic.php?t=20088700&amp;start=340">18</a>&nbsp;<a href="./viewtopic.php?t=20088700&amp;start=360">19</a>&nbsp;<a href="./viewtopic.php?t=20088700&amp;start=380">20</a></span> </p>
      </div> </td>
    </tr>
   </tbody>
  </table> </td>
 <td class="cost">
  <div class="price-primary">
   1680,00 р.
  </div> <small class="cost-torg"> торг </small> </td>
 <td class="lst post-tls">
  <div class="ba-topic-last-post-data" style="display:none">
    i-product
   <br>9 дней назад
  </div> <p class="ba-post-coms"> <a href="./viewtopic.php?t=20088700&amp;view=unread#unread" alt="i-product, 9 дней назад" title="i-product, 9 дней назад" class="b-ico i-sm-postnew"></a> <a href="./viewtopic.php?t=20088700&amp;view=unread#unread" alt="i-product, 9 дней назад" title="i-product, 9 дней назад" class="c-org"> 396</a> </p> <p class="ba-post-up"><small class="tot-up">UP!</small> 44 минуты назад</p> </td>
</tr>
        """) to Product(
            id = 20088700,
            location = "Минск",
            title = "iPhone 11/11 PRO/11 PRO MAX/XS/XS MAX/XR/X(10)",
            description = "100% ОРИГИНАЛ* Гарантийный Сертификат Официального Сервиса до 36 месяцев. Вся техника новая, запечатанная и не активированная ! 30 дней на проверку ! Возможен безналичный расчёт. В подарок портативная аудио система ! +375(29)6700266 viber",
            image = "https://content.onliner.by/baraholka/icon_large/20088700",
            timestamp = Timestamp(millisNow - 44 * TimeUnit.MINUTE.millis, TimeUnit.MINUTE),
            commentsCount = 0,
            isPaid = false,
            owner = ProductOwner("Onliner.by", 2565106),
            price = Price(null, false),
            type = ProductType.WARNING
        )
    ).map { (input, expected) ->
        DynamicTest.dynamicTest("Converting HTML to Product, id = ${expected.id}") {
            val actual = converter.convert(input)
            Assertions.assertEquals(expected, actual)
        }
    }
}
