package kt.school.starlord.ui.subcategories

import androidx.lifecycle.LiveData
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
    private val subcategories = MutableLiveData<List<Subcategory>>()

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
