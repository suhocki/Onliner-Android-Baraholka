package kt.school.starlord.model.system.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kt.school.starlord.domain.system.viewmodel.ProgressEmitter

/**
 * Implements ProgressEmitter.
 */
class ProgressViewModelFeature : ProgressEmitter {

    private val progress = MutableLiveData<Boolean>()

    override fun getProgress(): LiveData<Boolean> = progress

    /**
     * Apply visibility state represented by boolean to progress LiveData.
     * True if visible, false otherwise.
     */
    fun showProgress(visible: Boolean) {
        progress.value = visible
    }
}
