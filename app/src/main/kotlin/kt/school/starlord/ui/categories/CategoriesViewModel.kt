package kt.school.starlord.ui.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.launch
import kt.school.starlord.entity.Category
import kt.school.starlord.entity.Subcategory
import kt.school.starlord.model.network.NetworkRepository
import kt.school.starlord.model.room.RoomRepository

/**
 * Contains logic with fetching categories asynchronously.
 */
class CategoriesViewModel(
    private val networkRepository: NetworkRepository,
    private val roomRepository: RoomRepository
) : ViewModel() {
    private val categories = MutableLiveData<List<Category>>()
    private val error = LiveEvent<Throwable>()
    private val progress = MutableLiveData<Boolean>()

    init {
        roomRepository.getCategories().observeForever(categories::setValue)

        viewModelScope.launch {
            progress.value = true

            runCatching { networkRepository.getCategoriesWithSubcategories() }
                .fold({ updateDatabase(it) }, error::setValue)

            progress.value = false
        }
    }

    /**
     * Use for observing categories.
     */
    fun getCategories(): LiveData<List<Category>> = categories

    /**
     * LiveData for observing errors.
     */
    fun getError(): LiveData<Throwable> = error

    /**
     * LiveData for observing progress state.
     */
    fun getProgress(): LiveData<Boolean> = progress

    private suspend fun updateDatabase(categoriesWithSubcategories: Map<Category, List<Subcategory>>) {
        roomRepository.updateCategories(categoriesWithSubcategories.keys.toList())
        roomRepository.updateSubcategories(categoriesWithSubcategories.values.flatten())
    }
}
