package kt.school.starlord.ui.subcategories

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import kt.school.starlord.domain.SubcategoriesRepository
import kt.school.starlord.entity.Subcategory

/**
 * Contains logic with fetching data asynchronously
 */
class SubcategoriesViewModel(
    private val subcategoriesRepository: SubcategoriesRepository
) : ViewModel() {

    /**
     * Loads LiveData with subcategories for selected categoryName from the database
     *
     * @return LiveData with subcategories
     */
    fun getSubcategoriesLiveData(categoryName: String): LiveData<List<Subcategory>> {
        return subcategoriesRepository.getSubcategories(categoryName)
    }
}