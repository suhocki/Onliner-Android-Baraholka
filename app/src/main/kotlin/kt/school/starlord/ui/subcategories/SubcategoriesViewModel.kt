package kt.school.starlord.ui.subcategories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kt.school.starlord.domain.SubcategoriesRepository
import kt.school.starlord.entity.Subcategory

/**
 * Contains logic with fetching subcategories asynchronously.
 */
class SubcategoriesViewModel(
    private val subcategoriesRepository: SubcategoriesRepository
) : ViewModel() {
    private val subcategories = MutableLiveData<List<Subcategory>>()
    private val error = MutableLiveData<Throwable>()

    /**
     * LiveData for observing errors.
     */
    fun getErrors(): LiveData<Throwable> = error

    /**
     * Use for observing subcategories.
     */
    fun getSubcategories(): LiveData<List<Subcategory>> = subcategories

    /**
     * Loads subcategories for selected categoryName from the database.
     *
     * @return subcategories
     */
    fun loadSubcategories(categoryName: String) {
        viewModelScope.launch {
            runCatching { subcategoriesRepository.getSubcategories(categoryName) }
                .fold(subcategories::setValue, error::setValue)
        }
    }
}
