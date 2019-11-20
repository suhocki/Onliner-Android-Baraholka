package kt.school.starlord.ui.products

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import kotlinx.coroutines.launch
import kt.school.starlord.BuildConfig
import kt.school.starlord.domain.data.mapper.Mapper
import kt.school.starlord.domain.repository.product.ProductsCacheRepository
import kt.school.starlord.domain.repository.product.ProductsRepository
import kt.school.starlord.domain.system.viewmodel.ErrorEmitter
import kt.school.starlord.domain.system.viewmodel.ProgressEmitter
import kt.school.starlord.model.system.viewmodel.ErrorViewModelFeature
import kt.school.starlord.model.system.viewmodel.ProgressViewModelFeature
import kt.school.starlord.ui.global.entity.UiEntity
import kt.school.starlord.ui.subcategories.entity.UiSubcategory

/**
 * Contains logic with fetching products asynchronously.
 */
class ProductsViewModel(
    private val mapper: Mapper,
    private val progressFeature: ProgressViewModelFeature,
    private val errorFeature: ErrorViewModelFeature,
    private val networkRepository: ProductsRepository,
    private val databaseRepository: ProductsCacheRepository,
    private val subcategory: UiSubcategory
) : ViewModel(), ProgressEmitter by progressFeature, ErrorEmitter by errorFeature {

    private val uiEntityLiveData: LiveData<PagedList<UiEntity>>

    init {
        val factory = databaseRepository
            .getCachedProducts(subcategory.name)
            .map { mapper.map<UiEntity>(it) }

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPrefetchDistance(PREFETCH_DISTANCE)
            .setPageSize(BuildConfig.PAGE_SIZE)
            .build()

        uiEntityLiveData = LivePagedListBuilder(factory, config).build()

        refreshData()
    }

    /**
     * Use for observing products.
     */
    fun getProducts() = uiEntityLiveData

    private fun refreshData() {
        viewModelScope.launch {
            progressFeature.showProgress(true)

            runCatching {
                val products = networkRepository.getProducts(subcategory.link)
                databaseRepository.updateProducts(subcategory.name, products)
            }.onFailure(errorFeature::showError)

            progressFeature.showProgress(false)
        }
    }

    companion object {
        private const val PREFETCH_DISTANCE = 3
    }
}
