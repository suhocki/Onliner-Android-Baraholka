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

    private val subcategoriesRepository: SubcategoriesRepository = mockk()
    private val viewModel = SubcategoriesViewModel(subcategoriesRepository)

    @Test
    fun `load subcategories from database`() = testCoroutineRule.runBlockingTest {
        // Given
        val categoryName = "Apple"
        val subcategories: List<Subcategory> = mockk()
        coEvery { subcategoriesRepository.getSubcategories(categoryName) } coAnswers { subcategories }

        // When
        viewModel.loadSubcategories(categoryName)

        // Then
        coVerify(exactly = 1) { subcategoriesRepository.getSubcategories(categoryName) }
        viewModel.getSubcategories().observeForTesting {
            assert(viewModel.getSubcategories().value == subcategories)
        }
    }
}