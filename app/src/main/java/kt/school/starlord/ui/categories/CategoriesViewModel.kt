package kt.school.starlord.ui.categories

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kt.school.starlord.domain.CategoriesRepository
import kt.school.starlord.entity.Category

class CategoriesViewModel(
    private val categoriesRepository: CategoriesRepository
) : ViewModel() {

    /**
     * Use for observing categories
     */
    val categoriesLiveData = MutableLiveData<List<Category>>()

    /**
     * Load categories from repository and send them to categoriesLiveData
     */
    fun loadCategories() {
        viewModelScope.launch {
            val data = categoriesRepository.getCategories()
            categoriesLiveData.postValue(data)
        }
    }
}