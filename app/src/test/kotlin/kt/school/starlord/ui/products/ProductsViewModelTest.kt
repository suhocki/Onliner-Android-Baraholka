package kt.school.starlord.ui.products

import android.view.View
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.every
import io.mockk.mockk
import kt.school.starlord.domain.mapper.Mapper
import kt.school.starlord.domain.entity.product.Product
import kt.school.starlord.domain.repository.product.ProductsCacheRepository
import kt.school.starlord.domain.repository.product.ProductsNetworkRepository
import kt.school.starlord.model.system.viewmodel.ErrorViewModelFeature
import kt.school.starlord.model.system.viewmodel.ProgressViewModelFeature
import kt.school.starlord.ui.TestCoroutineRule
import kt.school.starlord.ui.createConverter
import kt.school.starlord.ui.createDataSource
import kt.school.starlord.ui.global.entity.UiEntity
import kt.school.starlord.ui.observeForTesting
import kt.school.starlord.ui.subcategories.entity.UiSubcategory
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyLong

/* ktlint-disable */
class ProductsViewModelTest {
    @get:Rule
    internal val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    internal val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val subcategoryId = anyLong()
    private val subcategory = UiSubcategory("subcategoryName", "123", subcategoryId, View.VISIBLE)
    private val errorFeature: ErrorViewModelFeature = mockk(relaxUnitFun = true)
    private val progressFeature: ProgressViewModelFeature = mockk(relaxUnitFun = true)
    private val networkRepository: ProductsNetworkRepository = mockk()
    private val databaseRepository: ProductsCacheRepository = mockk(relaxed = true)

    @Test
    fun loadProducts_fromCache() = testCoroutineRule.runBlockingTest {
        // Given
        val products: List<Product> = listOf(mockk(), mockk(), mockk())
        val expected: Array<UiEntity> = arrayOf(mockk(), mockk(), mockk())

        every { databaseRepository.getCachedProducts(subcategoryId) }.answers { createDataSource(products) }

        val viewModel = createViewModel(
            mapper = Mapper(setOf(createConverter(products.zip(expected).toMap()))),
            subcategory = subcategory,
            databaseRepository = databaseRepository
        )

        // When
        val productsLiveData = viewModel.getProducts()

        // Then
        productsLiveData.observeForTesting { assert(expected.contentEquals(it.toTypedArray())) }
    }

    @Test
    fun loadProducts_fromNetwork_updateCache() = testCoroutineRule.runBlockingTest {
        // Given
        val fetchedProducts: List<Product> = listOf(mockk())

        coEvery { networkRepository.downloadProductsPage(subcategoryId) }.coAnswers { fetchedProducts }

        // When
        createViewModel(
            subcategory = subcategory,
            databaseRepository = databaseRepository,
            networkRepository = networkRepository
        )

        // Then
        coVerify { databaseRepository.updateProducts(subcategoryId, fetchedProducts) }
    }

    @Test
    fun loadProducts_fromNetwork_showError() = testCoroutineRule.runBlockingTest {
        // Given
        val error = Throwable()

        coEvery { networkRepository.downloadProductsPage(any()) }.throws(error)

        // When
        createViewModel(errorFeature = errorFeature, networkRepository = networkRepository)

        // Then
        coVerify { errorFeature.showError(error) }
    }

    @Test
    fun loadProducts_fromNetwork_showProgress() = testCoroutineRule.runBlockingTest {
        // When
        createViewModel(progressFeature = progressFeature)

        // Then
        coVerifyOrder {
            progressFeature.showProgress(true)
            progressFeature.showProgress(false)
        }
    }

    private fun createViewModel(
        mapper: Mapper = mockk(relaxed = true),
        progressFeature: ProgressViewModelFeature = mockk(relaxed = true),
        errorFeature: ErrorViewModelFeature = mockk(relaxed = true),
        networkRepository: ProductsNetworkRepository = mockk(relaxed = true),
        databaseRepository: ProductsCacheRepository = mockk(relaxed = true),
        subcategory: UiSubcategory = mockk(relaxed = true)
    ) = ProductsViewModel(mapper, progressFeature, errorFeature, networkRepository, databaseRepository, subcategory)
}
