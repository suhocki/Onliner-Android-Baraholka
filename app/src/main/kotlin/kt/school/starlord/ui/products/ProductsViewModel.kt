package kt.school.starlord.ui.products

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import kotlinx.coroutines.launch
import kt.school.starlord.BuildConfig
import kt.school.starlord.domain.HtmlParser
import kt.school.starlord.domain.mapper.Mapper
import kt.school.starlord.domain.repository.product.ProductsCacheRepository
import kt.school.starlord.domain.repository.product.ProductsNetworkRepository
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
    private val parser: HtmlParser,
    private val progressFeature: ProgressViewModelFeature,
    private val errorFeature: ErrorViewModelFeature,
    private val networkRepository: ProductsNetworkRepository,
    private val databaseRepository: ProductsCacheRepository,
    private val subcategory: UiSubcategory
) : ViewModel(), ProgressEmitter by progressFeature, ErrorEmitter by errorFeature {

    private val uiEntityLiveData: LiveData<PagedList<UiEntity>>

    init {
        val factory = databaseRepository
            .getCachedProducts(subcategory.id)
            .map { mapper.map<UiEntity>(it) }

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(BuildConfig.PAGE_SIZE)
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
                val html = networkRepository.downloadProductsPage(subcategory.id)
                val products = parser.parseProducts(html)

                databaseRepository.updateProducts(subcategory.id, products)
            }.onFailure(errorFeature::showError)

            progressFeature.showProgress(false)
        }
    }

    companion object {
        private const val PREFETCH_DISTANCE = 3
    }
}
