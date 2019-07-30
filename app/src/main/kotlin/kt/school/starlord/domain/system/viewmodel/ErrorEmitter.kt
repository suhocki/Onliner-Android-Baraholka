package kt.school.starlord.domain.system.viewmodel

import androidx.lifecycle.LiveData

/**
 * Interface for receiving errors.
 */
interface ErrorEmitter {
    /**
     * LiveData for observing errors.
     */
    fun getError(): LiveData<Throwable>
}
