package kt.school.starlord

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.mockkStatic
import io.mockk.verify
import kt.school.starlord.extension.fixIMMLeak
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.AutoCloseKoinTest

@RunWith(AndroidJUnit4::class)
class AppActivityTest : AutoCloseKoinTest() {
    private val scenario by lazy {
        ActivityScenario.launch(AppActivity::class.java)
    }

    @Test
    fun `remove input method manager`() {
        mockkStatic("kt.school.starlord.extension.AndroidExtensionsKt")

        scenario.onActivity {
            scenario.moveToState(Lifecycle.State.DESTROYED)
            verify { it.fixIMMLeak() }
        }
    }
}
