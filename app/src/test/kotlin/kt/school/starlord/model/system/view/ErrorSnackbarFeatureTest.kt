package kt.school.starlord.model.system.view

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import kt.school.starlord.StarlordActivity
import kt.school.starlord.ui.checkSnackBarDisplayedByMessage
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.AutoCloseKoinTest
import org.mockito.ArgumentMatchers.anyString

@RunWith(AndroidJUnit4::class)
class ErrorSnackbarFeatureTest : AutoCloseKoinTest() {

    @Test
    fun show() {
        ActivityScenario.launch(StarlordActivity::class.java).onActivity {
            // Given
            val error = Throwable()
            val snackbarFeature = ErrorSnackbarFeature(it)

            // When
            snackbarFeature.show(error)

            // Then
            checkSnackBarDisplayedByMessage(error.toString())
        }
    }

    @Test(expected = Throwable::class)
    fun dismiss() {
        ActivityScenario.launch(StarlordActivity::class.java).onActivity {
            // Given
            val error = Throwable(anyString())
            val snackbarFeature = ErrorSnackbarFeature(it)

            snackbarFeature.show(error)

            Thread.sleep(1000)

            // When
            snackbarFeature.dismiss()

            // Then
            Espresso.onView(ViewMatchers.withId(com.google.android.material.R.id.snackbar_text))
                .check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
        }
    }
}
