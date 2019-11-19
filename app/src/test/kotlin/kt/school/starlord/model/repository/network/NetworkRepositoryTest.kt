package kt.school.starlord.model.repository.network

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import kt.school.starlord.BuildConfig
import kt.school.starlord.TestContextProvider
import kt.school.starlord.domain.data.mapper.Mapper
import kt.school.starlord.domain.entity.category.Category
import kt.school.starlord.domain.entity.product.Product
import kt.school.starlord.domain.entity.subcategory.Subcategory
import kt.school.starlord.ui.TestCoroutineRule
import kt.school.starlord.ui.createConverter
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import java.net.URL

class NetworkRepositoryTest {
    @get:Rule
    internal val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    internal val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val contextProvider = TestContextProvider()

    @Test
    fun getCategoriesWithSubcategories() = testCoroutineRule.runBlockingTest {
        // Given
        val category = Category("name")
        val subcategory: Subcategory = mockk(relaxUnitFun = true)
        val expected = mapOf(category to listOf(subcategory))

        val mapper = createMapper(category, subcategory)

        val networkRepository = NetworkRepository(mapper, contextProvider)

        // When
        val actual = networkRepository.getCategoriesWithSubcategories()

        // Then
        assert(actual == expected)
    }

    @Test
    fun getCategoriesWithSubcategories_fillCategoryNameInSubcategories() = testCoroutineRule.runBlockingTest {
        // Given
        val categoryName = "name"
        val category = Category(categoryName)
        val subcategory: Subcategory = mockk(relaxUnitFun = true)

        val mapper = createMapper(category, subcategory)

        val networkRepository = NetworkRepository(mapper, contextProvider)

        // When
        networkRepository.getCategoriesWithSubcategories()

        // Then
        verify { subcategory.categoryName = categoryName }
    }

    @Test
    fun getProducts() = testCoroutineRule.runBlockingTest {
        // Given
        val link = anyString()
        val expected: Product = mockk()
        val mapper = createMapper(expected, link)

        val networkRepository = NetworkRepository(mapper, contextProvider)

        // When
        val actual = networkRepository.getProducts(link).first()

        // Then
        assert(actual == expected)
    }

    private fun createMapper(category: Category, subcategory: Subcategory): Mapper {
        val document: Document = mockk()
        val categoryElement: Element = mockk()
        val subcategoryElement: Element = mockk()
        mockkStatic(Jsoup::class)

        every { Jsoup.parse(URL(BuildConfig.BARAHOLKA_ONLINER_URL), BuildConfig.NETWORK_REQUEST_TIMEOUT_MILLIS) }
            .answers { document }
        every { document.getElementsByClass("cm-onecat") } answers { Elements(categoryElement) }
        every { categoryElement.getElementsByClass("b-cm-list") } answers { Elements(subcategoryElement) }
        every { subcategoryElement.select("li") } answers { Elements(subcategoryElement) }

        return Mapper(
            setOf(
                createConverter(mapOf(categoryElement to category)),
                createConverter(mapOf(subcategoryElement to subcategory))
            )
        )
    }

    private fun createMapper(expected: Product, link: String): Mapper {
        val document: Document = mockk()
        val element: Element = mockk()
        val url: URL = mockk()

        mockkStatic(Jsoup::class)

        every { Jsoup.parse(url, BuildConfig.NETWORK_REQUEST_TIMEOUT_MILLIS) }.answers { document }
        every { document.getElementsByClass("ba-tbl-list__table") }.answers { Elements(element) }
        every { element.getElementsByTag("tr") }.answers { Elements(element) }
        every { element.getElementsByClass("wraptxt").hasText() }.answers { true }
        every { element.getElementsByClass("ba-signature").first() }.answers { mockk() }
        every { element.getElementsByClass("cost").first() }.answers { mockk() }

        return Mapper(
            setOf(
                createConverter(element to expected),
                createConverter(link to url)
            )
        )
    }
}
