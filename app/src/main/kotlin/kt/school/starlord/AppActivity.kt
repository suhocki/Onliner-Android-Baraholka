package kt.school.starlord

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_app.*
import kt.school.starlord.extension.removeInputMethodManager
import kt.school.starlord.model.system.SystemMessageNotifier
import kt.school.starlord.model.system.SystemMessageReceiver
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast
import org.koin.android.ext.android.inject
import timber.log.Timber

/**
 * The only activity class for this application.
 * Implement system notifier interface.
 */
class AppActivity : AppCompatActivity(), SystemMessageReceiver {

    private val systemMessageNotifier: SystemMessageNotifier by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app)
    }

    override fun onResume() {
        super.onResume()
        systemMessageNotifier.systemMessageReceiver = this
    }

    override fun onPause() {
        super.onPause()
        systemMessageNotifier.systemMessageReceiver = null
    }

    override fun onDestroy() {
        super.onDestroy()
        removeInputMethodManager()
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
