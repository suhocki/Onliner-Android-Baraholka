package kt.school.starlord.ui.subcategories

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kt.school.starlord.domain.SubcategoriesRepository
import kt.school.starlord.entity.Subcategory
import kt.school.starlord.ui.global.BaseViewModel

/**
 * Contains logic with fetching subcategories asynchronously.
 */
class SubcategoriesViewModel(
    private val subcategoriesRepository: SubcategoriesRepository
) : BaseViewModel() {

    /**
     * Use for observing subcategories.
     */
    val subcategories = MutableLiveData<List<Subcategory>>()

    /**
     * Loads subcategories for selected categoryName from the database.
     *
     * @return subcategories
     */
    fun loadSubcategories(categoryName: String) {
        viewModelScope.launch {
            progress.value = true
            runCatching { subcategoriesRepository.getSubcategories(categoryName) }
                .fold(subcategories::setValue, error::setValue)
            progress.value = false
        }
    }
}
