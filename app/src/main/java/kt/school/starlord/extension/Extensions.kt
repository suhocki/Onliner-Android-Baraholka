package kt.school.starlord.extension

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import kt.school.starlord.ui.global.SystemNotifier

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
 *  Cast activity to SystemNotifier.
 */
val Fragment.systemNotifier: SystemNotifier
    get() {
        val activity = requireActivity()
        return if (activity is SystemNotifier) activity else error("$activity is not a SystemNotifier")
    }
