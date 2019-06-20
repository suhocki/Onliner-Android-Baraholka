package kt.school.starlord.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

internal fun <T> LiveData<T>.observeForTesting(
    block: () -> Unit
) {
    val observer = Observer<T> { Unit }
    try {
        observeForever(observer)
        block()
    } finally {
        removeObserver(observer)
    }
}
