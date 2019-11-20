package kt.school.starlord.model.system.view

import android.app.Activity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_appbar.*
import kt.school.starlord.R
import kt.school.starlord.domain.system.view.ErrorSnackbar

/**
 * Implementation of ErrorSnackbar interface.
 */
class ErrorSnackbarFeature(activity: Activity) : ErrorSnackbar {

    private val snackbar by lazy {
        val coordinatorLayout = activity.coordinatorLayout
        Snackbar.make(coordinatorLayout, EMPTY_STRING, Snackbar.LENGTH_INDEFINITE)
            .apply {
                anchorView = activity.fab
                setAction(R.string.ok) { dismiss() }
            }
    }

    override fun show(error: Throwable) = with(snackbar) {
        setText("$error")
        show()
    }

    override fun dismiss() {
        snackbar.dismiss()
    }

    companion object {
        private const val EMPTY_STRING = ""
    }
}
