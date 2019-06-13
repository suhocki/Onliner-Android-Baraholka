package kt.school.starlord.ui.global

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

    override fun onDestroy() {
        super.onDestroy()
        fixInputMethod(this)
    }

    private fun fixInputMethod(context: Context) {
        var inputMethodManager: InputMethodManager? = null
        try {
            inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        } catch (th: Throwable) {
            th.printStackTrace()
        }
        if (inputMethodManager == null) {
            return
        }
        val declaredFields = inputMethodManager.javaClass.declaredFields
        for (declaredField in declaredFields) {
            try {
                if (!declaredField.isAccessible) {
                    declaredField.isAccessible = true
                }
                val obj = declaredField.get(inputMethodManager)
                if (obj == null || obj !is View) {
                    continue
                }
                val view: View = obj
                if (view.context === context) {
                    declaredField.set(inputMethodManager, null)
                } else {
                    return
                }
            } catch (th: Throwable) {
                th.printStackTrace()
            }
        }
    }
}