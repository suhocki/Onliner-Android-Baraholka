package kt.school.starlord.ui.global

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hadilq.liveevent.LiveEvent

/**
 * Base ViewModel.
 * Contains LiveData-objects and methods that can be used in any inherited ViewModel.
 */
abstract class BaseViewModel: ViewModel() {
    protected val error = LiveEvent<Throwable>()
    protected val progress = MutableLiveData<Boolean>()

    /**
     * LiveData for observing errors.
     */
    fun getError(): LiveData<Throwable> = error

    /**
     * LiveData for observing progress state.
     */
    fun getProgress(): LiveData<Boolean> = progress
}
