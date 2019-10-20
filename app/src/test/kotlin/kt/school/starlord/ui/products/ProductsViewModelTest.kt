package kt.school.starlord.ui.products

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import io.mockk.coEvery
import io.mockk.coVerifyOrder
import io.mockk.mockk
import kt.school.starlord.domain.repository.product.ProductsRepository
import kt.school.starlord.domain.repository.product.ProductsCacheRepository
import kt.school.starlord.domain.entity.product.Product
import kt.school.starlord.domain.entity.subcategory.Subcategory
import kt.school.starlord.model.data.mapper.Mapper
import kt.school.starlord.model.data.mapper.converter.ProductToUiProductConverter
import kt.school.starlord.model.repository.mock.MockRepository
import kt.school.starlord.model.system.viewmodel.ErrorViewModelFeature
import kt.school.starlord.model.system.viewmodel.ProgressViewModelFeature
import kt.school.starlord.ui.TestCoroutineRule
import kt.school.starlord.ui.observeForTesting
import org.junit.Rule
import org.junit.Test

/* ktlint-disable */
class ProductsViewModelTest {
    @get:Rule
    internal val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    internal val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val link = "https://baraholka.onliner.by/viewforum.php?f=2"
    private val subcategoryName = "subcategoryName"
    private val subcategory = Subcategory(subcategoryName, "", 0, link)
    private val errorFeature: ErrorViewModelFeature = mockk(relaxUnitFun = true)
    private val progressFeature: ProgressViewModelFeature = mockk(relaxUnitFun = true)
    private val productsListRepository: ProductsRepository = mockk()
    private val mapper = Mapper(setOf(ProductToUiProductConverter(mockk(relaxed = true))))
    private val productsRepository: ProductsCacheRepository = mockk(relaxUnitFun = true)
    private val mockRepository = MockRepository()

    @Test
    fun `refresh data by network successfully`() = testCoroutineRule.runBlockingTest {
        // Given
        val products: List<Product> = mockRepository.products

        coEvery { productsRepository.getProductsLiveData(subcategoryName) }.coAnswers { MutableLiveData(products) }
        coEvery { productsListRepository.getProducts(link) }.coAnswers { products }

        // When
        ProductsViewModel(mapper, progressFeature, errorFeature, productsListRepository, productsRepository, subcategory)

        // Then
        coVerifyOrder {
            progressFeature.showProgress(true)
            productsRepository.updateProducts(subcategoryName, products)
            progressFeature.showProgress(false)
        }
    }

    @Test
    fun `refresh data by network failure`() = testCoroutineRule.runBlockingTest {
        // Given
        val error = Throwable()
        val products: List<Product> = mockRepository.products

        coEvery { productsRepository.getProductsLiveData(subcategoryName) }.coAnswers { MutableLiveData(products) }
        coEvery { productsListRepository.getProducts(any()) }.throws(error)

        // When
        ProductsViewModel(mapper, progressFeature, errorFeature, productsListRepository, productsRepository, subcategory)

        // Then
        coVerifyOrder {
            progressFeature.showProgress(true)
            errorFeature.showError(error)
            progressFeature.showProgress(false)
        }
    }

    @Test
    fun `load data from database`() {
        // Given
        val products = mockRepository.products

        coEvery { productsRepository.getProductsLiveData(subcategoryName) }.coAnswers { MutableLiveData(products) }
        coEvery { productsListRepository.getProducts(link) }.coAnswers { products }

        // When
        val viewModel = ProductsViewModel(
            mapper,
            progressFeature,
            errorFeature,
            productsListRepository,
            productsRepository,
            subcategory
        )

        // Then
        viewModel.getProducts().observeForTesting {
            assert(it.size == products.size)
        }
    }
}
