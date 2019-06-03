package kt.school.starlord.ui.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch
import kt.school.starlord.domain.CategoriesRepository
import kt.school.starlord.entity.Category

class CategoriesViewModel(
    private val categoriesRepository: CategoriesRepository
) : ViewModel() {

    private val categoriesDeferred = CompletableDeferred<List<Category>>()

    suspend fun loadCategories(): List<Category> {
        viewModelScope.launch {
            val note = categoriesRepository.getCategories()
            categoriesDeferred.complete(note)
        }
        return categoriesDeferred.await()
    }
}