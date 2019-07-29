package kt.school.starlord.domain.system.viewmodel

import androidx.lifecycle.LiveData

/**
 * Interface for receiving progress events.
 */
interface ProgressEmitter {
    /**
     * LiveData for observing progress state.
     */
    fun getProgress(): LiveData<Boolean>
}
