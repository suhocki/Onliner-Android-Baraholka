package kt.school.starlord.model.system.view

import android.app.Activity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_appbar.*
import kt.school.starlord.R
import kt.school.starlord.domain.system.view.ProgressSnackbar

/**
 * Implementation of ProgressSnackbar interface.
 */
class ProgressSnackbarFeature(activity: Activity) : ProgressSnackbar {

    private val snackbar by lazy {
        val coordinatorLayout = activity.coordinatorLayout
        Snackbar.make(coordinatorLayout, R.string.updating, Snackbar.LENGTH_INDEFINITE)
            .apply { anchorView = activity.fab }
    }

    override fun setVisibility(visible: Boolean) {
        if (visible) snackbar.show() else snackbar.dismiss()
    }
}
