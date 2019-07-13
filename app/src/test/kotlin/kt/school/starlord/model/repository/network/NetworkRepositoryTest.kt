package kt.school.starlord.model.repository.network

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.coEvery
import io.mockk.mockk
import kt.school.starlord.TestContextProvider
import kt.school.starlord.entity.Category
import kt.school.starlord.entity.Subcategory
import kt.school.starlord.model.data.network.OnlinerApi
import kt.school.starlord.model.data.parser.PageParser
import kt.school.starlord.ui.TestCoroutineRule
import org.junit.Rule
import org.junit.Test

class NetworkRepositoryTest {
    @get:Rule
    internal val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    internal val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val pageLoader: OnlinerApi = mockk()
    private val parser: PageParser = mockk()

    private val networkRepository =
        NetworkRepository(pageLoader, parser, TestContextProvider())

    @Test
    fun `get categories with subcategories`() = testCoroutineRule.runBlockingTest {
        // Given
        val page = "page"
        val data: Map<Category, List<Subcategory>> = mockk()

        coEvery { pageLoader.loadPage() } coAnswers { page }
        coEvery { parser.parseCategories(page) } coAnswers { data }

        // When
        val answer = networkRepository.getCategoriesWithSubcategories()

        // Then
        assert(answer == data)
    }
}
