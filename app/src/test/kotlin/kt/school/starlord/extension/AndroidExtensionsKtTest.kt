package kt.school.starlord.extension

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.Test
import timber.log.Timber
import java.lang.reflect.Field

class AndroidExtensionsKtTest {

    @Test
    fun `fix input manager memory leak`() {
        // Given
        val activity: Activity = mockk()
        val inputMethodManager: InputMethodManager = mockk()
        val declaredField: Field = mockk(relaxUnitFun = true)
        val view: View = mockk()

        mockkStatic("kt.school.starlord.extension.ReflectExtensionsKt")

        every { activity.getSystemService(Context.INPUT_METHOD_SERVICE) } returns inputMethodManager
        every { inputMethodManager.getClassDeclaredFields() } returns arrayOf(declaredField)
        every { declaredField.get(inputMethodManager) } returns view
        every { view.context } returns activity

        // When
        activity.fixIMMLeak()

        // Then
        verify {
            declaredField.isAccessible = true
            declaredField.set(inputMethodManager, null)
        }
    }

    @Test
    fun `show error`() {
        // Given
        mockkStatic(Timber::class)
        mockkStatic(Toast::class)
        mockkStatic("org.jetbrains.anko.ToastsKt")

        val errorMessage = "errorMessage"
        val context: Context = mockk()
        val error: Throwable = mockk()
        val toast: Toast = mockk(relaxUnitFun = true)

        every { error.toString() } returns errorMessage
        every { Toast.makeText(context, errorMessage, Toast.LENGTH_LONG) } returns toast

        // When
        context.showError(error)

        // Then
        verify {
            Timber.e(error)
            toast.show()
        }
    }

    @Test
    fun `set view visible`() {
        // Given
        val view: View = mockk(relaxUnitFun = true)

        // When
        view.setVisible(true)

        // Then
        verify { view.visibility = View.VISIBLE }
    }

    @Test
    fun `set view invisible`() {
        // Given
        val view: View = mockk(relaxUnitFun = true)

        // When
        view.setVisible(false)

        // Then
        verify { view.visibility = View.GONE }
    }
}
