package kt.school.starlord.ui.products

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.coEvery
import io.mockk.mockk
import kt.school.starlord.domain.repository.ProductsListRepository
import kt.school.starlord.entity.product.Product
import kt.school.starlord.model.system.viewmodel.ErrorViewModelFeature
import kt.school.starlord.model.system.viewmodel.ProgressViewModelFeature
import kt.school.starlord.entity.product.ProductsList
import kt.school.starlord.ui.TestCoroutineRule
import kt.school.starlord.ui.observeForTesting
import org.junit.Rule
import org.junit.Test

class ProductsViewModelTest {
    @get:Rule
    internal val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    internal val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val link = "https://baraholka.onliner.by/viewforum.php?f=2"
    private val errorFeature: ErrorViewModelFeature = mockk(relaxUnitFun = true)
    private val progressFeature: ProgressViewModelFeature = mockk(relaxUnitFun = true)
    private val productsRepository: ProductsListRepository = mockk()

    @Test
    fun `get products`() {
        // Given
        val products: List<Product> = mockk()

        coEvery { productsRepository.getProducts(link) }.coAnswers { ProductsList(products) }

        // When
        val viewModel = ProductsViewModel(progressFeature, errorFeature, productsRepository, link)

        // Then
        viewModel.getProducts().observeForTesting {
            assert(it == products)
        }
    }
}
