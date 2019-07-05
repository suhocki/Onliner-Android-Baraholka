package kt.school.starlord.ui.subcategories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kt.school.starlord.domain.SubcategoriesRepository
import kt.school.starlord.entity.Subcategory

/**
 * Contains logic with fetching subcategories asynchronously.
 */
class SubcategoriesViewModel(
    subcategoriesRepository: SubcategoriesRepository,
    categoryName: String
) : ViewModel() {
    private val subcategories = MutableLiveData<List<Subcategory>>()

    init {
        subcategoriesRepository.getSubcategories(categoryName).observeForever(subcategories::setValue)
    }

    /**
     * Use for observing subcategories.
     */
    fun getSubcategories(): LiveData<List<Subcategory>> = subcategories
}
