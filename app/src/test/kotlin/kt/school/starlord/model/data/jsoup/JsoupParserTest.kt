package kt.school.starlord.model.data.jsoup

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kt.school.starlord.TestContextProvider
import kt.school.starlord.ui.TestCoroutineRule
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.ArgumentMatchers.anyString
import java.io.InputStream
import java.nio.charset.StandardCharsets

internal class JsoupParserTest {
    @get:Rule
    internal val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    internal val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val document: Document = mockk()
    private val dataSource = JsoupParser(TestContextProvider())

    private val categoryElement: Element = mockk()
    private val subcategoryElement: Element = mockk()

    @Test
    fun getCategoriesElements() = testCoroutineRule.runBlockingTest {
        //given
        val expected = mapOf(categoryElement to listOf(subcategoryElement))

        createCategoriesMocks()

        //when
        val actual = dataSource.parseCategories()

        //then
        assert(expected == actual)
    }

    @Test
    fun getProductElements() = testCoroutineRule.runBlockingTest {
        //given
        val element: Element = mockk()
        val expected = listOf(element)
        val forumId = anyLong()
        val inputStream: InputStream = mockk(relaxUnitFun = true)

        mockkStatic(Jsoup::class)

        every { Jsoup.parse(any<InputStream>(), StandardCharsets.UTF_8.name(), anyString()) } returns document

        every { uriManager.getProductsInputStream(forumId) }.answers { inputStream }
        every { document.getElementsByClass("ba-tbl-list__table") }.answers { Elements(element) }

        every { element.getElementsByTag("tr") }.answers { Elements(element) }
        every { element.getElementsByClass("wraptxt").hasText() }.answers { true }
        every { element.getElementsByClass("ba-signature").first() }.answers { mockk() }
        every { element.getElementsByClass("cost").first() }.answers { mockk() }

        //when
        val actual = dataSource.getProductElements(forumId)

        //then
        assert(expected == actual)
    }

    private fun createCategoriesMocks() {
        val inputStream: InputStream = mockk()

        every { uriManager.getCategoriesInputStream() } answers { inputStream }
        every { Jsoup.parse(inputStream, StandardCharsets.UTF_8.name(), anyString()) }.answers { document }
        every { document.getElementsByClass("cm-onecat") } answers { Elements(categoryElement) }
        every { categoryElement.getElementsByClass("b-cm-list") } answers { Elements(subcategoryElement) }
        every { subcategoryElement.select("li") } answers { Elements(subcategoryElement) }
    }
}