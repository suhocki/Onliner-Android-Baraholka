package kt.school.starlord.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

inline fun <reified T> LiveData<T>.observeForTesting(
    block: (T) -> Unit
) {
    val observer = Observer<T> { Unit }
    try {
        observeForever(observer)
        block(value as T)
    } finally {
        removeObserver(observer)
    }
}
