package kt.school.starlord.ui.products

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kt.school.starlord.domain.repository.product.ProductsRepository
import kt.school.starlord.domain.repository.product.ProductsCacheRepository
import kt.school.starlord.domain.system.viewmodel.ErrorEmitter
import kt.school.starlord.domain.system.viewmodel.ProgressEmitter
import kt.school.starlord.domain.entity.subcategory.Subcategory
import kt.school.starlord.model.data.mapper.Mapper
import kt.school.starlord.model.system.viewmodel.ErrorViewModelFeature
import kt.school.starlord.model.system.viewmodel.ProgressViewModelFeature
import kt.school.starlord.ui.products.entity.UiProduct

/**
 * Contains logic with fetching products asynchronously.
 */
class ProductsViewModel(
    private val mapper: Mapper,
    private val progressFeature: ProgressViewModelFeature,
    private val errorFeature: ErrorViewModelFeature,
    private val networkRepository: ProductsRepository,
    private val databaseRepository: ProductsCacheRepository,
    private val subcategory: Subcategory
) : ViewModel(), ProgressEmitter by progressFeature, ErrorEmitter by errorFeature {

    private val products = MutableLiveData<List<UiProduct>>()

    init {
        databaseRepository.getProductsLiveData(subcategory.name)
            .map { products -> products.map { mapper.map<UiProduct>(it) } }
            .observeForever(products::setValue)

        refreshData()
    }

    /**
     * Use for observing products.
     */
    fun getProducts(): LiveData<List<UiProduct>> = products

    private fun refreshData() {
        viewModelScope.launch {
            progressFeature.showProgress(true)

            runCatching { networkRepository.getProducts(subcategory.link) }
                .fold({ databaseRepository.updateProducts(subcategory.name, it) }, errorFeature::showError)

            progressFeature.showProgress(false)
        }
    }
}
