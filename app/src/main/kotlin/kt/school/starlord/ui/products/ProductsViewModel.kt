package kt.school.starlord.ui.products

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kt.school.starlord.domain.repository.ProductsListRepository
import kt.school.starlord.entity.product.Product

/**
 * Contains logic with fetching products asynchronously.
 */
class ProductsViewModel(
    productsRepository: ProductsListRepository,
    link: String
) : ViewModel() {
    private val products = MutableLiveData<List<Product>>()
    private val error = LiveEvent<Throwable>()
    private val progress = MutableLiveData<Boolean>()

    init {
        GlobalScope.launch(Dispatchers.Main) {
            products.setValue(
                productsRepository.getProducts(subcategory = link).products
            )
        }
    }

    /**
     * Use for observing products.
     */
    fun getProducts(): LiveData<List<Product>> = products

    /**
     * LiveData for observing errors.
     */
    fun getError(): LiveData<Throwable> = error

    /**
     * LiveData for observing progress state.
     */
    fun getProgress(): LiveData<Boolean> = progress
}
