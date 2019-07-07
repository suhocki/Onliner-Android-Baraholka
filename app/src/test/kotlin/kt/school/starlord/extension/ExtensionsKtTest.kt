package kt.school.starlord.extension

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.Test
import java.lang.reflect.Field

class ExtensionsKtTest {

    @Test
    fun `fix input manager memory leak`() {
        // Given
        val activity: Activity = mockk()
        val inputMethodManager: InputMethodManager = mockk()
        val declaredField: Field = mockk(relaxUnitFun = true)
        val view: View = mockk()

        mockkStatic("kt.school.starlord.extension.ExtensionsKt")

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
}
