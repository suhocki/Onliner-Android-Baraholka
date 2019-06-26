package kt.school.starlord.extension

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

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
