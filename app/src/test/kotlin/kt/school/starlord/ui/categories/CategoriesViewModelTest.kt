package kt.school.starlord.ui.categories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.every
import io.mockk.mockk
import kt.school.starlord.domain.entity.category.Category
import kt.school.starlord.domain.entity.subcategory.Subcategory
import kt.school.starlord.domain.repository.CategoriesCacheRepository
import kt.school.starlord.domain.repository.CategoriesWithSubcategoriesRepository
import kt.school.starlord.domain.repository.SubcategoriesRepository
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
    private val categoriesWithSubcategories = mapOf<Category, List<Subcategory>>(
        mockk<Category>() to listOf(mockk(), mockk()),
        mockk<Category>() to listOf(mockk())
    )

    @Test
    fun loadCategories_fromCache() = testCoroutineRule.runBlockingTest {
        // Given
        val categories: List<Category> = mockk()

        every { categoriesRepository.getCategories() }.answers { MutableLiveData(categories) }

        // When
        val viewModel = createViewModel(categoriesRepository = categoriesRepository)

        // Then
        viewModel.getCategories().observeForTesting { assert(it == categories) }
    }

    @Test
    fun loadCategories_fromNetwork_updateCache() = testCoroutineRule.runBlockingTest {
        // Given
        coEvery { networkRepository.getCategoriesWithSubcategories() }.coAnswers { categoriesWithSubcategories }

        // When
        createViewModel(networkRepository = networkRepository, categoriesRepository = categoriesRepository)

        // Then
        coVerify { categoriesRepository.updateCategories(categoriesWithSubcategories.keys.toList()) }
    }

    @Test
    fun loadSubcategories_fromNetwork_updateCache() = testCoroutineRule.runBlockingTest {
        // Given
        coEvery { networkRepository.getCategoriesWithSubcategories() }.coAnswers { categoriesWithSubcategories }

        // When
        createViewModel(networkRepository = networkRepository, subcategoriesRepository = subcategoriesRepository)

        // Then
        coVerify { subcategoriesRepository.updateSubcategories(categoriesWithSubcategories.values.flatten()) }
    }

    @Test
    fun loadCategories_fromNetwork_showProgress() = testCoroutineRule.runBlockingTest {
        // When
        createViewModel(progressFeature = progressFeature)

        // Then
        coVerifyOrder {
            progressFeature.showProgress(true)
            progressFeature.showProgress(false)
        }
    }

    @Test
    fun loadCategories_fromNetwork_showError() = testCoroutineRule.runBlockingTest {
        // Given
        val error = Throwable()

        coEvery { networkRepository.getCategoriesWithSubcategories() }.throws(error)

        // When
        createViewModel(errorFeature = errorFeature, networkRepository = networkRepository)

        // Then
        coVerifyOrder { errorFeature.showError(error) }
    }

    private fun createViewModel(
        progressFeature: ProgressViewModelFeature = mockk(relaxed = true),
        errorFeature: ErrorViewModelFeature = mockk(relaxed = true),
        networkRepository: CategoriesWithSubcategoriesRepository = mockk(relaxed = true),
        categoriesRepository: CategoriesCacheRepository = mockk(relaxed = true),
        subcategoriesRepository: SubcategoriesRepository = mockk(relaxed = true)
    ) = CategoriesViewModel(
        progressFeature,
        errorFeature,
        networkRepository,
        categoriesRepository,
        subcategoriesRepository
    )
}
