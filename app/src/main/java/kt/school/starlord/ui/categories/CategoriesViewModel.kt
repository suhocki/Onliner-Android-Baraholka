package kt.school.starlord.ui.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kt.school.starlord.domain.CategoriesRepository
import kt.school.starlord.entity.Category

class CategoriesViewModel(
    private val categoriesRepository: CategoriesRepository
) : ViewModel() {

    private val categories by lazy {
        MutableLiveData<List<Category>>().also {
            loadCategories()
        }
    }

    fun getCategories(): LiveData<List<Category>> {
        return categories
    }

    private fun loadCategories() {
        GlobalScope.launch {
            val data = categoriesRepository.getCategories()
            categories.postValue(data)
        }
    }
}