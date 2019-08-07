package kt.school.starlord.ui.subcategories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kt.school.starlord.domain.repository.SubcategoriesRepository
import kt.school.starlord.entity.subcategory.Subcategory

/**
 * Contains logic with fetching subcategories asynchronously.
 */
class SubcategoriesViewModel(
    databaseRepository: SubcategoriesRepository,
    categoryName: String
) : ViewModel() {
    private val subcategories = MutableLiveData<List<Subcategory>>()

    init {
        databaseRepository.getSubcategories(categoryName).observeForever(subcategories::setValue)
    }

    /**
     * Use for observing subcategories.
     */
    fun getSubcategories(): LiveData<List<Subcategory>> = subcategories
}
