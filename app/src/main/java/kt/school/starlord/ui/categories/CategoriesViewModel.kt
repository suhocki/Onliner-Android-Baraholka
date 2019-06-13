package kt.school.starlord.ui.categories

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kt.school.starlord.domain.CategoriesRepository
import kt.school.starlord.domain.SubcategoriesRepository
import kt.school.starlord.entity.Category

class CategoriesViewModel(
    private val remoteCategoriesRepository: CategoriesRepository,
    private val localCategoriesRepository: CategoriesRepository,
    private val localSubcategoriesRepository: SubcategoriesRepository
) : ViewModel() {

    /**
     * Use for observing categories
     */
    val categoriesLiveData = MutableLiveData<List<Category>>()

    /**
     * Loads categories from the database
     */
    fun loadLocalCategories() {
        viewModelScope.launch {
            val data = localCategoriesRepository.getCategories()
            categoriesLiveData.postValue(data)
        }
    }

    /**
     * Loads categories (and subcategories) from the Internet and puts them in a database
     */
    fun loadRemoteCategories() {
        viewModelScope.launch {
            val map = remoteCategoriesRepository.getCategoriesWithSubcategories()
            val categories = map.keys.toList()

            localCategoriesRepository.updateCategories(categories)
            localSubcategoriesRepository.updateSubcategories(map.values.flatten())

            categoriesLiveData.postValue(categories)
        }
    }
}