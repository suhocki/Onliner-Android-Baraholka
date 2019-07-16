package kt.school.starlord

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kt.school.starlord.extension.removeInputMethodManager

/**
 * The only activity class for this application.
 * Implement system notifier interface.
 */
class AppActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app)
    }

    override fun onDestroy() {
        super.onDestroy()
        removeInputMethodManager()
    }
}
