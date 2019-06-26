package kt.school.starlord.ui.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kt.school.starlord.entity.Category
import kt.school.starlord.model.network.NetworkRepository
import kt.school.starlord.model.room.RoomRepository
import kt.school.starlord.ui.global.BaseViewModel

/**
 * Contains logic with fetching categories asynchronously.
 */
class CategoriesViewModel(
    private val networkRepository: NetworkRepository,
    private val roomRepository: RoomRepository
) : BaseViewModel() {
    private val categories = MutableLiveData<List<Category>>()

    /**
     * Use for observing categories.
     */
    fun getCategories(): LiveData<List<Category>> = categories

    /**
     * Loads categories from the database.
     */
    fun loadLocalCategories() {
        viewModelScope.launch {
            runCatching { roomRepository.getCategories() }.fold(categories::setValue, error::setValue)
        }
    }

    /**
     * Loads categories (and subcategories) from the Internet and puts them in a database.
     */
    fun loadRemoteCategories() {
        viewModelScope.launch {
            progress.value = true

            runCatching {
                val categoriesWithSubcategories = networkRepository.getCategoriesWithSubcategories()
                val categories = categoriesWithSubcategories.keys.toList()
                roomRepository.updateCategories(categories)
                roomRepository.updateSubcategories(categoriesWithSubcategories.values.flatten())
                categories
            }.fold(categories::setValue, error::setValue)

            progress.value = false
        }
    }
}
