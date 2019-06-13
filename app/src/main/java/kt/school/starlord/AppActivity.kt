package kt.school.starlord

import android.os.Bundle
import kt.school.starlord.ui.global.BaseActivity

class AppActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
