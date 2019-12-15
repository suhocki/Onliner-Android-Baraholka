package kt.school.starlord

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kt.school.starlord.ui.global.extension.fixIMMLeak

/**
 * The only activity class for this application.
 * Implement system notifier interface.
 */
class StarlordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app)
    }

    override fun onDestroy() {
        super.onDestroy()
        fixIMMLeak()
    }
}
