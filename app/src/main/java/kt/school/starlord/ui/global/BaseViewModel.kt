package kt.school.starlord.ui.global

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * Base ViewModel that keeps common logic for other ViewModels.
 */
abstract class BaseViewModel : ViewModel() {

    /**
     * LiveData for observing progress state.
     */
    val progress = MutableLiveData<Boolean>()
    /**
     * LiveData for observing errors.
     */
    val error = MutableLiveData<Throwable>()
}
