package kt.school.starlord

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import kt.school.starlord.extension.removeInputMethodManager
import kt.school.starlord.model.system.SystemMessageNotifier
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.AutoCloseKoinTest
import org.koin.test.mock.declare

@RunWith(AndroidJUnit4::class)
class AppActivityTest : AutoCloseKoinTest() {
    private val systemMessageNotifier: SystemMessageNotifier = mockk(relaxUnitFun = true)
    private val scenario by lazy {
        ActivityScenario.launch(AppActivity::class.java)
    }

    @Before
    fun setUp() {
        declare {
            single { systemMessageNotifier }
        }
    }

    @Test
    fun `listen to system messages`() {
        scenario.onActivity {
            verify { systemMessageNotifier.systemMessageReceiver = it }
        }
    }

    @Test
    fun `remove system messages listener`() {
        scenario.onActivity {
            scenario.moveToState(Lifecycle.State.DESTROYED)
            verify { systemMessageNotifier.systemMessageReceiver = null }
        }
    }

    @Test
    fun `remove input method manager`() {
        mockkStatic("kt.school.starlord.extension.ExtensionsKt")

        scenario.onActivity {
            scenario.moveToState(Lifecycle.State.DESTROYED)
            verify { it.removeInputMethodManager() }
        }
    }
}
