package kt.school.starlord.extension

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import timber.log.Timber

/**
 * Encapsulate logic for creating a view using layoutRes.
 * @param layoutRes resource that represents XML layout
 */
fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(
        layoutRes,
        this,
        attachToRoot
    )
}

/**
 * Fix InputManager memory leak.
 * https://code.google.com/p/android/issues/detail?id=171190
 */
fun Activity.fixIMMLeak() {
    val inputMethodManager =
        runCatching { getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager }
            .onFailure(Timber::e)
            .getOrNull() ?: return

    inputMethodManager.javaClass.declaredFields.forEach { field ->
        runCatching {
            field.isAccessible = true
            (field.get(inputMethodManager) as? View)?.let { view ->
                if (view.context === this) {
                    field.set(inputMethodManager, null)
                }
            }
        }.onFailure(Timber::e)
    }
}
