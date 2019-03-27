package kt.school.starlord.ui.categories

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kt.school.starlord.domain.CategoriesRepository
import kt.school.starlord.entity.Category

class CategoriesViewModel(
    private val categoriesRepository: CategoriesRepository,
    private val coroutineScope: CoroutineScope = GlobalScope
) : ViewModel() {

    val categories = MutableLiveData<List<Category>>()

    fun loadCategories() {
        coroutineScope.launch {
            val data = categoriesRepository.getCategories()
            categories.postValue(data)
        }
    }
}