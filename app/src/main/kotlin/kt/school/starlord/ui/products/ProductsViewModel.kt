package kt.school.starlord.ui.products

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import kotlinx.coroutines.launch
import kt.school.starlord.BuildConfig
import kt.school.starlord.domain.entity.subcategory.Subcategory
import kt.school.starlord.domain.repository.product.ProductsCacheRepository
import kt.school.starlord.domain.repository.product.ProductsRepository
import kt.school.starlord.domain.system.viewmodel.ErrorEmitter
import kt.school.starlord.domain.system.viewmodel.ProgressEmitter
import kt.school.starlord.domain.data.mapper.Mapper
import kt.school.starlord.model.system.viewmodel.ErrorViewModelFeature
import kt.school.starlord.model.system.viewmodel.ProgressViewModelFeature
import kt.school.starlord.ui.global.entity.UiEntity

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

    private val uiEntityLiveData: LiveData<PagedList<UiEntity>>

    init {
        val factory = databaseRepository
            .getProductsCached(subcategory.name)
            .map { mapper.map<UiEntity>(it) }

        uiEntityLiveData = LivePagedListBuilder(factory, BuildConfig.PAGE_SIZE).build()

        refreshData()
    }

    /**
     * Use for observing products.
     */
    fun getProducts() = uiEntityLiveData

    private fun refreshData() {
        viewModelScope.launch {
            progressFeature.showProgress(true)

            runCatching { networkRepository.getProducts(subcategory.link) }
                .fold({ databaseRepository.updateProducts(subcategory.name, it) }, errorFeature::showError)

            progressFeature.showProgress(false)
        }
    }
}
