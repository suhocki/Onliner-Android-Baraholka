package kt.school.starlord.model.data.jsoup

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kt.school.starlord.BuildConfig
import kt.school.starlord.TestContextProvider
import kt.school.starlord.ui.TestCoroutineRule
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.net.URL

internal class JsoupDataSourceTest {
    @get:Rule
    internal val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    internal val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val document: Document = mockk()
    private val dataSource = JsoupDataSource(TestContextProvider())

    @Before
    fun setUp() {
        mockkStatic(Jsoup::class)
    }

    @Test
    fun getCategoriesElements() = testCoroutineRule.runBlockingTest {
        //given
        val categoryElement: Element = mockk()
        val subcategoryElement: Element = mockk()
        val expected = mapOf(categoryElement to listOf(subcategoryElement))

        every {
            Jsoup.parse(URL(BuildConfig.BARAHOLKA_ONLINER_URL), BuildConfig.NETWORK_REQUEST_TIMEOUT_MILLIS)
        }.answers { document }

        every { document.getElementsByClass("cm-onecat") } answers { Elements(categoryElement) }
        every { categoryElement.getElementsByClass("b-cm-list") } answers { Elements(subcategoryElement) }
        every { subcategoryElement.select("li") } answers { Elements(subcategoryElement) }

        //when
        val actual = dataSource.getCategoriesElements()

        //then
        assert(expected == actual)
    }

    @Test
    fun getProductElements() = testCoroutineRule.runBlockingTest {
        //given
        val url: URL = mockk()
        val element: Element = mockk()
        val expected = listOf(element)

        every { Jsoup.parse(url, BuildConfig.NETWORK_REQUEST_TIMEOUT_MILLIS) }.answers { document }
        every { document.getElementsByClass("ba-tbl-list__table") }.answers { Elements(element) }
        every { element.getElementsByTag("tr") }.answers { Elements(element) }
        every { element.getElementsByClass("wraptxt").hasText() }.answers { true }
        every { element.getElementsByClass("ba-signature").first() }.answers { mockk() }
        every { element.getElementsByClass("cost").first() }.answers { mockk() }

        //when
        val actual = dataSource.getProductElements(url)

        //then
        assert(expected == actual)
    }
}