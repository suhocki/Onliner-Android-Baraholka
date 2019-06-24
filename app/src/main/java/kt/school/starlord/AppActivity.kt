package kt.school.starlord

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import kt.school.starlord.ui.global.BaseActivity
import kt.school.starlord.ui.global.SystemNotifier
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast
import timber.log.Timber

/**
 * The only activity class for this application.
 * Implement system notifier interface.
 */
class AppActivity : BaseActivity(), SystemNotifier {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun showMessage(message: String) {
        toast(message)
    }

    override fun showError(error: Throwable) {
        Timber.e(error)
        longToast(error.toString())
    }

    override fun showProgress(visible: Boolean) {
        progressBar.visibility = if (visible) View.VISIBLE else View.INVISIBLE
    }
}
