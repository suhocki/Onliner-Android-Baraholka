package kt.school.starlord.ui.global

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * Base ViewModel that keeps common logic for other ViewModels.
 */
abstract class BaseViewModel : ViewModel() {

    protected val progress = MutableLiveData<Boolean>()
    protected val error = MutableLiveData<Throwable>()

    /**
     * LiveData for observing progress state.
     */
    fun getProgress(): LiveData<Boolean> = progress

    /**
     * LiveData for observing errors.
     */
    fun getErrors(): LiveData<Throwable> = error
}
