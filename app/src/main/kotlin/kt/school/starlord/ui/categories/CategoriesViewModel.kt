package kt.school.starlord.ui.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
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

    init {
        roomRepository.getCategories().observeForever(categories::setValue)

        viewModelScope.launch {
            progress.value = true
            loadRemoteCategories()
            progress.value = false
        }
    }

    /**
     * Use for observing categories.
     */
    fun getCategories(): LiveData<List<Category>> = categories

    private suspend fun loadRemoteCategories() {
        try {
            val categoriesWithSubcategories = networkRepository.getCategoriesWithSubcategories()
            val categories = categoriesWithSubcategories.keys.toList()

            roomRepository.updateCategories(categories)
            roomRepository.updateSubcategories(categoriesWithSubcategories.values.flatten())
        } catch (throwable: Throwable) {
            error.value = throwable
        }
    }
}
