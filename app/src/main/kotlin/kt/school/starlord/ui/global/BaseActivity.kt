package kt.school.starlord.ui.global

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity

/**
 * Class with method for exclude "won't fix" memory leak
 * (INPUT_METHOD_MANAGER__SERVED_VIEW,
 * https://code.google.com/p/android/issues/detail?id=171190)
 */
abstract class BaseActivity : AppCompatActivity() {

    override fun onDestroy() {
        super.onDestroy()
        fixMemoryLeak(this)
    }

    private fun fixMemoryLeak(context: Context) {
        val inputMethodManager =
                try {
                    (context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)
                } catch (th: KotlinNullPointerException) {
                    th.printStackTrace()
                } ?: return

        val declaredFields = inputMethodManager.javaClass.declaredFields
        for (declaredField in declaredFields) {
            try {
                if (!declaredField.isAccessible) {
                    declaredField.isAccessible = true
                }
                val view = (declaredField.get(inputMethodManager) ?: continue) as? View ?: continue
                if (view.context === context) {
                    declaredField.set(inputMethodManager, null)
                } else {
                    return
                }
            } catch (th: KotlinNullPointerException) {
                th.printStackTrace()
            }
        }
    }
}
