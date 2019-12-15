package kt.school.starlord

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.mockkStatic
import io.mockk.verify
import kt.school.starlord.ui.global.extension.fixIMMLeak
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.AutoCloseKoinTest

@RunWith(AndroidJUnit4::class)
class StarlordStarlordActivityTest : AutoCloseKoinTest() {
    private val scenario by lazy {
        ActivityScenario.launch(StarlordActivity::class.java)
    }

    @Test
    fun removeInputMethodManager() {
        mockkStatic("kt.school.starlord.ui.global.extension.AndroidExtensionsKt")

        scenario.onActivity {
            scenario.moveToState(Lifecycle.State.DESTROYED)
            verify { it.fixIMMLeak() }
        }
    }
}
