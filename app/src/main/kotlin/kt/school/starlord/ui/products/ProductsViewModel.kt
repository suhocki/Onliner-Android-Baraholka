package kt.school.starlord.ui.products

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kt.school.starlord.domain.repository.ProductsRepository
import kt.school.starlord.domain.system.viewmodel.ErrorEmitter
import kt.school.starlord.domain.system.viewmodel.ProgressEmitter
import kt.school.starlord.entity.product.Product
import kt.school.starlord.model.system.viewmodel.ErrorViewModelFeature
import kt.school.starlord.model.system.viewmodel.ProgressViewModelFeature

/**
 * Contains logic with fetching products asynchronously.
 */
class ProductsViewModel(
    private val progressFeature: ProgressViewModelFeature,
    private val errorFeature: ErrorViewModelFeature,
    productsRepository: ProductsRepository,
    subcategoryName: String
) : ViewModel(), ProgressEmitter by progressFeature, ErrorEmitter by errorFeature {

    private val products = MutableLiveData<List<Product>>()

    init {
        productsRepository.getProducts(subcategoryName).observeForever(products::setValue)
    }

    /**
     * Use for observing products.
     */
    fun getProducts(): LiveData<List<Product>> = products
}
