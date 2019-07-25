package kt.school.starlord.ui.categories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kt.school.starlord.domain.repository.CategoriesRepository
import kt.school.starlord.domain.repository.CategoriesWithSubcategoriesRepository
import kt.school.starlord.domain.repository.SubcategoriesRepository
import kt.school.starlord.entity.CategoriesWithSubcategories
import kt.school.starlord.entity.Category
import kt.school.starlord.entity.Subcategory
import kt.school.starlord.ui.TestCoroutineRule
import kt.school.starlord.ui.observeForTesting
import org.junit.Rule
import org.junit.Test

class CategoriesViewModelTest {

    @get:Rule
    internal val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    internal val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val networkRepository: CategoriesWithSubcategoriesRepository = mockk()
    private val categoriesRepository: CategoriesRepository = mockk(relaxed = true)
    private val subcategoriesRepository: SubcategoriesRepository = mockk(relaxed = true)

    @Test
    fun `refresh data by network`() = testCoroutineRule.runBlockingTest {
        // Given
        val categories: List<Category> = mockk()
        val subcategories: List<Subcategory> = mockk()
        val categoriesWithSubcategories = CategoriesWithSubcategories(categories, subcategories)
        coEvery { networkRepository.getCategoriesWithSubcategories() }.coAnswers { categoriesWithSubcategories }

        // When
        CategoriesViewModel(networkRepository, categoriesRepository, subcategoriesRepository)

        // Then
        coVerify(exactly = 1) {
            subcategoriesRepository.updateSubcategories(subcategories)
            categoriesRepository.updateCategories(categories)
        }
    }

    @Test
    fun `load data from database`() = testCoroutineRule.runBlockingTest {
        // Given
        val categories: List<Category> = mockk()
        val categoriesLiveData = MutableLiveData(categories)

        every { categoriesRepository.getCategories() }.answers { categoriesLiveData }

        // When
        val viewModel = CategoriesViewModel(networkRepository, categoriesRepository, subcategoriesRepository)

        // Then
        viewModel.getCategories().observeForTesting {
            assert(it == categories)
        }
    }
}
