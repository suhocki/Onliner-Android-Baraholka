package kt.school.starlord.ui.products

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kt.school.starlord.domain.repository.product.ProductsRepository
import kt.school.starlord.domain.repository.product.ProductsCacheRepository
import kt.school.starlord.domain.system.viewmodel.ErrorEmitter
import kt.school.starlord.domain.system.viewmodel.ProgressEmitter
import kt.school.starlord.entity.product.Product
import kt.school.starlord.entity.subcategory.Subcategory
import kt.school.starlord.model.system.viewmodel.ErrorViewModelFeature
import kt.school.starlord.model.system.viewmodel.ProgressViewModelFeature

/**
 * Contains logic with fetching products asynchronously.
 */
class ProductsViewModel(
    private val progressFeature: ProgressViewModelFeature,
    private val errorFeature: ErrorViewModelFeature,
    private val networkRepository: ProductsRepository,
    private val databaseRepository: ProductsCacheRepository,
    private val subcategory: Subcategory
) : ViewModel(), ProgressEmitter by progressFeature, ErrorEmitter by errorFeature {

    private val products = MutableLiveData<List<Product>>()

    init {
        databaseRepository.getProductsLiveData(subcategory.name).observeForever(products::setValue)
        refreshData()
    }

    /**
     * Use for observing products.
     */
    fun getProducts(): LiveData<List<Product>> = products

    private fun refreshData() {
        viewModelScope.launch {
            progressFeature.showProgress(true)

            runCatching { networkRepository.getProducts(subcategory.link) }
                .fold({ databaseRepository.updateProducts(subcategory.name, it) }, errorFeature::showError)

            progressFeature.showProgress(false)
        }
    }
}
