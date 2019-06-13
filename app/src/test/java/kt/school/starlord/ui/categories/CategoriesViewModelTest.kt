package kt.school.starlord.ui.categories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.coEvery
import io.mockk.mockk
import kt.school.starlord.domain.CategoriesRepository
import kt.school.starlord.domain.SubcategoriesRepository
import kt.school.starlord.entity.Category
import org.junit.Rule
import org.junit.Test

internal class CategoriesViewModelTest {

    @get:Rule
    internal val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    internal val instantTaskExecutorRule = InstantTaskExecutorRule()

    private var remoteCategoriesRepository: CategoriesRepository = mockk()
    private var localCategoriesRepository: CategoriesRepository = mockk()
    private var localSubcategoriesRepository: SubcategoriesRepository = mockk()

    private var viewModel = CategoriesViewModel(
        remoteCategoriesRepository,
        localCategoriesRepository,
        localSubcategoriesRepository
    )

    @Test
    fun `load categories successfully`() = testCoroutineRule.runBlockingTest {
        // Given that the CategoriesRepository returns the list of categories
        val testCategories: List<Category> = mockk()
        coEvery { localCategoriesRepository.getCategories() }.coAnswers { testCategories }

        // When loading categories
        viewModel.loadLocalCategories()

        // The correct list of categories is emitted
        viewModel.categoriesLiveData.observeForTesting {
            assert(viewModel.categoriesLiveData.value == testCategories) { "Emitted data is wrong" }
        }
    }
}