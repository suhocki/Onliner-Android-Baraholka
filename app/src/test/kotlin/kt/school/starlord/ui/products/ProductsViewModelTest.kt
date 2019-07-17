package kt.school.starlord.ui.products

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import io.mockk.every
import io.mockk.mockk
import kt.school.starlord.domain.repository.ProductsRepository
import kt.school.starlord.entity.product.Product
import kt.school.starlord.ui.TestCoroutineRule
import kt.school.starlord.ui.observeForTesting
import org.junit.Rule
import org.junit.Test

class ProductsViewModelTest {
    @get:Rule
    internal val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    internal val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val subcategoryName = "subcategoryName"
    private val productsRepository: ProductsRepository = mockk()

    @Test
    fun `get products from database`() {
        // Given
        val products: List<Product> = mockk()

        every { productsRepository.getProducts(subcategoryName) } returns MutableLiveData(products)

        // When
        val viewModel = ProductsViewModel(productsRepository, subcategoryName)

        // Then
        viewModel.getProducts().observeForTesting {
            assert(it == products)
        }
    }
}
