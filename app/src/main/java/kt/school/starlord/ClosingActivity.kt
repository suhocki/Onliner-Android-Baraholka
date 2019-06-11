package kt.school.starlord

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class ClosingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Handler().postDelayed({ finish() }, 500)
    }
}