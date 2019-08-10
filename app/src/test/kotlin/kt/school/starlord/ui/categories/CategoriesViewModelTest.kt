package kt.school.starlord.ui.categories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import io.mockk.coEvery
import io.mockk.coVerifyOrder
import io.mockk.every
import io.mockk.mockk
import kt.school.starlord.domain.repository.CategoriesCacheRepository
import kt.school.starlord.domain.repository.CategoriesWithSubcategoriesRepository
import kt.school.starlord.domain.repository.SubcategoriesRepository
import kt.school.starlord.entity.category.CategoriesWithSubcategories
import kt.school.starlord.entity.category.Category
import kt.school.starlord.entity.subcategory.Subcategory
import kt.school.starlord.model.system.viewmodel.ErrorViewModelFeature
import kt.school.starlord.model.system.viewmodel.ProgressViewModelFeature
import kt.school.starlord.ui.TestCoroutineRule
import kt.school.starlord.ui.observeForTesting
import org.junit.Rule
import org.junit.Test

class CategoriesViewModelTest {

    @get:Rule
    internal val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    internal val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val errorFeature: ErrorViewModelFeature = mockk(relaxUnitFun = true)
    private val progressFeature: ProgressViewModelFeature = mockk(relaxUnitFun = true)
    private val networkRepository: CategoriesWithSubcategoriesRepository = mockk()
    private val categoriesRepository: CategoriesCacheRepository = mockk(relaxed = true)
    private val subcategoriesRepository: SubcategoriesRepository = mockk(relaxed = true)

    @Test
    fun `refresh data by network successfully`() = testCoroutineRule.runBlockingTest {
        // Given
        val categories: List<Category> = mockk()
        val subcategories: List<Subcategory> = mockk()
        val categoriesWithSubcategories =
            CategoriesWithSubcategories(categories, subcategories)
        coEvery { networkRepository.getCategoriesWithSubcategories() }.coAnswers { categoriesWithSubcategories }

        // When
        CategoriesViewModel(
            progressFeature,
            errorFeature,
            networkRepository,
            categoriesRepository,
            subcategoriesRepository
        )

        // Then
        coVerifyOrder {
            progressFeature.showProgress(true)

            categoriesRepository.updateCategories(categories)
            subcategoriesRepository.updateSubcategories(subcategories)

            progressFeature.showProgress(false)
        }
    }

    @Test
    fun `refresh data by network failure`() = testCoroutineRule.runBlockingTest {
        // Given
        val error = Throwable()
        coEvery { networkRepository.getCategoriesWithSubcategories() }.throws(error)

        // When
        CategoriesViewModel(
            progressFeature,
            errorFeature,
            networkRepository,
            categoriesRepository,
            subcategoriesRepository
        )

        // Then
        coVerifyOrder {
            progressFeature.showProgress(true)
            errorFeature.showError(error)
            progressFeature.showProgress(false)
        }
    }

    @Test
    fun `load data from database`() = testCoroutineRule.runBlockingTest {
        // Given
        val categories: List<Category> = mockk()
        val categoriesLiveData = MutableLiveData(categories)

        every { categoriesRepository.getCategoriesLiveData() }.answers { categoriesLiveData }

        // When
        val viewModel = CategoriesViewModel(
            progressFeature,
            errorFeature,
            networkRepository,
            categoriesRepository,
            subcategoriesRepository
        )

        // Then
        viewModel.getCategories().observeForTesting {
            assert(it == categories)
        }
    }
}
