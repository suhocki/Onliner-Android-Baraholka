package kt.school.starlord.model.system.view

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import kt.school.starlord.AppActivity
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.AutoCloseKoinTest
import org.mockito.ArgumentMatchers.anyString

@RunWith(AndroidJUnit4::class)
class ErrorSnackbarFeatureTest : AutoCloseKoinTest() {
    private val scenario by lazy {
        ActivityScenario.launch(AppActivity::class.java)
    }

    @Test
    fun `show error text on snackbar`() {
        // Given
        val error = Throwable(anyString())

        scenario.onActivity {
            val snackbarFeature = ErrorSnackbarFeature(it)

            // When
            snackbarFeature.show(error)

            // Then
            onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText("$error")))
        }
    }

    @Test(expected = NoMatchingViewException::class)
    fun `dismiss snackbar`() {
        // Given
        val error = Throwable(anyString())

        scenario.onActivity {
            val snackbarFeature = ErrorSnackbarFeature(it)

            snackbarFeature.show(error)

            Thread.sleep(1000)

            // When
            snackbarFeature.dismiss()

            // Then
            onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
        }
    }
}
