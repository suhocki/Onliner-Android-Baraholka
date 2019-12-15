package kt.school.starlord.model.data.jsoup

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kt.school.starlord.TestContextProvider
import kt.school.starlord.domain.entity.category.Category
import kt.school.starlord.domain.entity.product.Product
import kt.school.starlord.domain.entity.subcategory.Subcategory
import kt.school.starlord.domain.mapper.Mapper
import kt.school.starlord.ui.TestCoroutineRule
import kt.school.starlord.ui.createConverter
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString

internal class JsoupParserTest {
    @get:Rule
    internal val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    internal val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val document: Document = mockk()

    private val categoryElement: Element = mockk()
    private val subcategoryElement: Element = mockk()
    private val productElement: Element = mockk()

    private val category = mockk<Category>(relaxed = true)
    private val subcategory = mockk<Subcategory>(relaxUnitFun = true)
    private val product: Product = mockk()

    private val mapper = Mapper(
        setOf(
            createConverter(productElement to product),
            createConverter(mapOf(categoryElement to category)),
            createConverter(mapOf(subcategoryElement to subcategory))
        )
    )

    private val dataSource = JsoupParser(TestContextProvider(), mapper)

    @Before
    fun setUp() {
        mockkStatic(Jsoup::class)

        every { Jsoup.parse(anyString()) } returns document

        every { document.getElementsByClass("cm-onecat") } answers { Elements(categoryElement) }
        every { document.getElementsByClass("ba-tbl-list__table") }.answers { Elements(productElement) }

        every { categoryElement.getElementsByClass("b-cm-list") } answers { Elements(subcategoryElement) }

        every { subcategoryElement.select("li") } answers { Elements(subcategoryElement) }

        every { productElement.getElementsByTag("tr") }.answers { Elements(productElement) }
        every { productElement.getElementsByClass("wraptxt").hasText() }.answers { true }
        every { productElement.getElementsByClass("ba-signature").first() }.answers { mockk() }
        every { productElement.getElementsByClass("cost").first() }.answers { mockk() }
    }

    @Test
    fun getCategoriesElements() = testCoroutineRule.runBlockingTest {
        //when
        val actual = dataSource.parseCategories(anyString())

        //then
        assert(mapOf(category to listOf(subcategory)) == actual)
    }

    @Test
    fun parseProducts() = testCoroutineRule.runBlockingTest {
        //when
        val actual = dataSource.parseProducts(anyString())

        //then
        assert(listOf(product) == actual)
    }
}