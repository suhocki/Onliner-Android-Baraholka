package kt.school.starlord.ui.categories

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kt.school.starlord.entity.Category
import kt.school.starlord.model.network.NetworkRepository
import kt.school.starlord.model.room.RoomRepository

class CategoriesViewModel(
    private val networkRepository: NetworkRepository,
    private val roomRepository: RoomRepository
) : ViewModel() {

    /**
     * Use for observing categories
     */
    val categoriesLiveData = MutableLiveData<List<Category>>()

    /**
     * Loads categories from the database
     */
    fun loadLocalCategories() {
        viewModelScope.launch {
            val data = roomRepository.getCategories()
            categoriesLiveData.postValue(data)
        }
    }

    /**
     * Loads categories (and subcategories) from the Internet and puts them in a database
     */
    fun loadRemoteCategories() {
        viewModelScope.launch {
            val map = networkRepository.getCategoriesWithSubcategories()
            val categories = map.keys.toList()

            roomRepository.updateCategories(categories)
            roomRepository.updateSubcategories(map.values.flatten())

            categoriesLiveData.postValue(categories)
        }
    }
}