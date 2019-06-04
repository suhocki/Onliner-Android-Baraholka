package kt.school.starlord.ui.categories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.mockk
import kt.school.starlord.domain.CategoriesRepository
import kt.school.starlord.entity.Category
import org.junit.Before
import org.junit.Rule
import org.junit.Test

internal class CategoriesViewModelTest {

    @get:Rule
    internal val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    internal val instantTaskExecutorRule = InstantTaskExecutorRule()

    private var categoriesRepository: CategoriesRepository = mockk()

    private var viewModel = CategoriesViewModel(categoriesRepository)

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
    }

    @Test
    fun `load categories successfully`() = testCoroutineRule.runBlockingTest {
        // Given that the CategoriesRepository returns the list of categories
        val testedCategories: List<Category> = mockk()
        coEvery { categoriesRepository.getCategories() }.coAnswers { testedCategories }

        // When loading categories
        val realCategories = viewModel.loadCategories()

        // The correct list of categories is emitted
        assert(realCategories == testedCategories) { "Emitted data is wrong" }
    }
}