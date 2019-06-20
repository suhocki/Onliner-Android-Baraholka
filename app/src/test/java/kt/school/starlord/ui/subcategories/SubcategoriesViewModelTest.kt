package kt.school.starlord.ui.subcategories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kt.school.starlord.domain.SubcategoriesRepository
import kt.school.starlord.entity.Subcategory
import kt.school.starlord.ui.TestCoroutineRule
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
        val mockedSubcategories: List<Subcategory> = mockk()
        coEvery { subcategoriesRepository.getSubcategories(categoryName) } coAnswers { mockedSubcategories }

        // When
        val actualSubcategories = viewModel.getSubcategoriesLiveData(categoryName)

        // Then
        coVerify(exactly = 1) { subcategoriesRepository.getSubcategories(categoryName) }
        assert(actualSubcategories == mockedSubcategories)
    }
}
