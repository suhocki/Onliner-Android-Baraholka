package kt.school.starlord

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class AppActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        // This is important : Hack to open a dummy activity for 500ms
        //  and transparent floating activity and auto finishes)
        startActivity(Intent(this, ClosingActivity::class.java))
        finish()
    }
}
