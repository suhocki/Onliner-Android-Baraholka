package kt.school.starlord.ui.subcategories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kt.school.starlord.domain.SubcategoriesRepository
import kt.school.starlord.entity.Subcategory
import kt.school.starlord.ui.TestCoroutineRule
import kt.school.starlord.ui.observeForTesting
import org.junit.Rule
import org.junit.Test

class SubcategoriesViewModelTest {

    @get:Rule
    internal val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    internal val instantTaskExecutorRule = InstantTaskExecutorRule()

    private var subcategoriesRepository: SubcategoriesRepository = mockk()
    private var viewModel = SubcategoriesViewModel(subcategoriesRepository)

    @Test
    fun `load categories with subcategories by network`() = testCoroutineRule.runBlockingTest {
        // Given
        val categoryName = "Apple"
        val subcategories: List<Subcategory> = mockk()
        coEvery { subcategoriesRepository.getSubcategories(categoryName) } coAnswers { subcategories }

        // When
        viewModel.loadSubcategories(categoryName)

        // Then
        coVerify(exactly = 1) { subcategoriesRepository.getSubcategories(categoryName) }
        viewModel.subcategories.observeForTesting {
            assert(viewModel.subcategories.value == subcategories)
        }
    }
}
