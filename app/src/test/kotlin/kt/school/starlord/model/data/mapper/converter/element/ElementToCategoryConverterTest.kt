package kt.school.starlord.model.data.mapper.converter.element

import kt.school.starlord.domain.entity.category.Category
import org.jsoup.Jsoup
import org.jsoup.parser.Parser
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory

internal class ElementToCategoryConverterTest {

    private val converter = ElementToCategoryConverter()

    @TestFactory
    fun convertElements() = mapOf(
        createElement(
            "<div class=\"cm-onecat\"> <h3>Телефоны. Смартфоны</h3> <small class=\"show-new\">новое за <a href=\"/search.php?type=lastposts&amp;time=86400&amp;r=1\">24 часа</a></small> <ul class=\"b-cm-list\"> <li> <a href=\"./viewforum.php?f=2\">Мобильные телефоны</a> <sup> 6947 </sup> </li> <li> <a href=\"./viewforum.php?f=38\">Мобильные телефоны: аксессуары и запчасти</a> <sup> 7010 </sup> </li> <li> <a href=\"./viewforum.php?f=1131\">Мобильные телефоны: ремонт и другие услуги </a> <sup> 520 </sup> </li> <li> <a href=\"./viewforum.php?f=177\">Радиотелефоны, DECT и Проводные телефоны.</a> <sup> 862 </sup> </li> </ul> </div>"
        ) to Category("Телефоны. Смартфоны"),
        createElement(
            "<div class=\"cm-onecat\"> <h3>Ноутбуки. Компьютеры. Apple. Оргтехника</h3> <small class=\"show-new\">новое за <a href=\"/search.php?type=lastposts&amp;time=86400&amp;r=2\">24 часа</a></small> <ul class=\"b-cm-list\"> <li> <a href=\"./viewforum.php?f=63\">Ноутбуки</a> <sup> 2401 </sup> </li> <li> <a href=\"./viewforum.php?f=405\">Планшеты и электронные книги</a> <sup> 1107 </sup> </li> <li> <a href=\"./viewforum.php?f=621\">Ноутбуки, планшеты, электронные книги: запчасти и аксессуары</a> <sup> 5084 </sup> </li> <li> <a href=\"./viewforum.php?f=1178\">Ноутбуки, планшеты, электронные книги: ремонт и другие услуги </a> <sup> 294 </sup> </li> <li> <a href=\"./viewforum.php?f=643\">Apple. Mac. iPod. iPhone. iPad</a> <sup> 2052 </sup> </li> <li> <a href=\"./viewforum.php?f=50\">Apple: запчасти и аксессуары </a> <sup> 1270 </sup> </li> <li> <a href=\"./viewforum.php?f=1137\">Apple: ремонт и другие услуги </a> <sup> 147 </sup> </li> <li> <a href=\"./viewforum.php?f=285\">Материнские платы. Процессоры. Оперативная память</a> <sup> 4152 </sup> </li> <li> <a href=\"./viewforum.php?f=286\">Видеокарты</a> <sup> 1435 </sup> </li> <li> <a href=\"./viewforum.php?f=184\">USB-флешки. HDD. SSD. Чистые CD/DVD/Blu-ray диски и приводы</a> <sup> 1756 </sup> </li> <li> <a href=\"./viewforum.php?f=17\">Другие компьютерные комплектующие</a> <sup> 1833 </sup> </li> <li> <a href=\"./viewforum.php?f=181\">Корпуса. Блоки питания. ИБП(UPS). Системы охлаждения. Моддинг</a> <sup> 1920 </sup> </li> <li> <a href=\"./viewforum.php?f=185\">Мониторы. Проекторы</a> <sup> 1442 </sup> </li> <li> <a href=\"./viewforum.php?f=212\">Акустика. Клавиатуры. Мыши. Аксессуары</a> <sup> 1741 </sup> </li> <li> <a href=\"./viewforum.php?f=183\">Сетевое оборудование. Модемы. Wi-Fi</a> <sup> 2073 </sup> </li> <li> <a href=\"./viewforum.php?f=186\">Принтеры. МФУ. Офисная техника</a> <sup> 2199 </sup> </li> <li> <a href=\"./viewforum.php?f=1138\">Заправка картриджей. Ремонт принтеров и другие услуги</a> <sup> 488 </sup> </li> <li> <a href=\"./viewforum.php?f=180\">Сборка. Услуги. Сервис. Готовые компьютеры и серверы</a> <sup> 1674 </sup> </li> <li> <a href=\"./viewforum.php?f=210\">ПО и Игры для PС и Mac</a> <sup> 397 </sup> </li> <li> <a href=\"./viewforum.php?f=239\">Радиотехника и электроника</a> <sup> 5488 </sup> </li> </ul> </div>"
        ) to Category("Ноутбуки. Компьютеры. Apple. Оргтехника"),
        createElement(
            "<div class=\"cm-onecat\"> <h3>Интернет</h3> <small class=\"show-new\">новое за <a href=\"/search.php?type=lastposts&amp;time=86400&amp;r=3\">24 часа</a></small> <ul class=\"b-cm-list\"> <li> <a href=\"./viewforum.php?f=187\">Интернет-услуги. Электронная коммерция</a> <sup> 610 </sup> </li> <li> <a href=\"./viewforum.php?f=1144\">Создание сайтов</a> <sup> 610 </sup> </li> <li> <a href=\"./viewforum.php?f=1139\">Продвижение сайтов</a> <sup> 270 </sup> </li> </ul> </div>"
        ) to Category("Интернет")
    ).map { (input, expected) ->
        DynamicTest.dynamicTest("Converting HTML to Category: ${expected.name}") {
            val actual = converter.convert(input)
            Assertions.assertEquals(expected, actual)
        }
    }

    private fun createElement(htmlPart: String) =
        Jsoup.parse(htmlPart, "", Parser.xmlParser()).select("div").first()
}