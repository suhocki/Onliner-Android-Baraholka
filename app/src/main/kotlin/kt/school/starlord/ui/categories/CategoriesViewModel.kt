package kt.school.starlord.ui.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kt.school.starlord.domain.HtmlParser
import kt.school.starlord.domain.entity.category.Category
import kt.school.starlord.domain.repository.category.CategoriesCacheRepository
import kt.school.starlord.domain.repository.category.CategoriesNetworkRepository
import kt.school.starlord.domain.repository.category.SubcategoriesCacheRepository
import kt.school.starlord.domain.system.viewmodel.ErrorEmitter
import kt.school.starlord.domain.system.viewmodel.ProgressEmitter
import kt.school.starlord.model.system.viewmodel.ErrorViewModelFeature
import kt.school.starlord.model.system.viewmodel.ProgressViewModelFeature

/**
 * Contains logic with fetching categories asynchronously.
 */
class CategoriesViewModel(
    private val progressFeature: ProgressViewModelFeature,
    private val errorFeature: ErrorViewModelFeature,
    private val categoriesNetworkRepository: CategoriesNetworkRepository,
    private val categoriesCacheRepository: CategoriesCacheRepository,
    private val subcategoriesCacheRepository: SubcategoriesCacheRepository,
    private val htmlParser: HtmlParser
) : ViewModel(), ProgressEmitter by progressFeature, ErrorEmitter by errorFeature {

    private val categories = MutableLiveData<List<Category>>()

    init {
        categoriesCacheRepository.getCategories().observeForever(categories::setValue)
        refreshCategories()
    }

    /**
     * Use for observing categories.
     */
    fun getCategories(): LiveData<List<Category>> = categories

    private fun refreshCategories() = viewModelScope.launch {
        progressFeature.showProgress(true)

        runCatching {
            val html = categoriesNetworkRepository.downloadCategoriesPage()
            val categories = htmlParser.parseCategories(html)

            categoriesCacheRepository.updateCategories(categories.keys.toList())
            subcategoriesCacheRepository.updateSubcategories(categories.values.flatten())
        }.onFailure(errorFeature::showError)

        progressFeature.showProgress(false)
    }
}
