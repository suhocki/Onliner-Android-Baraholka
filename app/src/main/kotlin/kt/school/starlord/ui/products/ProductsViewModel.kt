package kt.school.starlord.ui.products

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hadilq.liveevent.LiveEvent
import kt.school.starlord.domain.repository.ProductsRepository
import kt.school.starlord.entity.product.Product

/**
 * Contains logic with fetching products asynchronously.
 */
class ProductsViewModel(
    productsRepository: ProductsRepository,
    subcategoryName: String
): ViewModel() {
    private val products = MutableLiveData<List<Product>>()
    private val error = LiveEvent<Throwable>()
    private val progress = MutableLiveData<Boolean>()

    init {
        productsRepository.getProducts(subcategoryName).observeForever(products::setValue)
    }

    /**
     * Use for observing products.
     */
    fun getSubcategories(): LiveData<List<Product>> = products

    /**
     * LiveData for observing errors.
     */
    fun getError(): LiveData<Throwable> = error

    /**
     * LiveData for observing progress state.
     */
    fun getProgress(): LiveData<Boolean> = progress
}
