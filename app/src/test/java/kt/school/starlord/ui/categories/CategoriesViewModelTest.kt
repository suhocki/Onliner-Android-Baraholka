package kt.school.starlord.ui.categories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.*
import kt.school.starlord.domain.CategoriesRepository
import kt.school.starlord.entity.Category
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CategoriesViewModelTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private var categoriesRepository: CategoriesRepository = mockk()

    private var viewModel = CategoriesViewModel(categoriesRepository)

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
    }

    @Test
    fun loadCategories() = testCoroutineRule.runBlockingTest {
        // Given that the CategoriesRepository returns the list of categories
        val categories: List<Category> = listOf(Category("name", listOf()))
        coEvery { categoriesRepository.getCategories() }.coAnswers { categories }

        // When loading categories
        val answer = viewModel.loadCategories()

        // The correct list of categories is emitted
        assert(answer == categories)
    }
}