package kt.school.starlord.ui.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kt.school.starlord.domain.entity.category.Category
import kt.school.starlord.domain.repository.CategoriesCacheRepository
import kt.school.starlord.domain.repository.CategoriesWithSubcategoriesRepository
import kt.school.starlord.domain.repository.SubcategoriesRepository
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
    private val networkRepository: CategoriesWithSubcategoriesRepository,
    private val categoriesRepository: CategoriesCacheRepository,
    private val subcategoriesRepository: SubcategoriesRepository
) : ViewModel(), ProgressEmitter by progressFeature, ErrorEmitter by errorFeature {

    private val categories = MutableLiveData<List<Category>>()

    init {
        categoriesRepository.getCategories().observeForever(categories::setValue)

        refreshData()
    }

    /**
     * Use for observing categories.
     */
    fun getCategories(): LiveData<List<Category>> = categories

    private fun refreshData() {
        viewModelScope.launch {
            progressFeature.showProgress(true)

            runCatching {
                val data = networkRepository.getCategoriesWithSubcategories()

                categoriesRepository.updateCategories(data.keys.toList())
                subcategoriesRepository.updateSubcategories(data.values.flatten())
            }.onFailure(errorFeature::showError)

            progressFeature.showProgress(false)
        }
    }
}
