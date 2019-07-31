package kt.school.starlord.model.system.view

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import kt.school.starlord.AppActivity
import kt.school.starlord.R
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.AutoCloseKoinTest

@RunWith(AndroidJUnit4::class)
class ProgressSnackbarFeatureTest : AutoCloseKoinTest() {
    private val scenario by lazy {
        ActivityScenario.launch(AppActivity::class.java)
    }

    @Test
    fun `show progress snackbar`() {
        scenario.onActivity {
            // Given
            val snackbarFeature = ProgressSnackbarFeature(it)

            // When
            snackbarFeature.setVisibility(true)

            // Then
            Espresso.onView(ViewMatchers.withId(com.google.android.material.R.id.snackbar_text))
                .check(ViewAssertions.matches(ViewMatchers.withText(R.string.updating)))
        }
    }

    @Test(expected = NoMatchingViewException::class)
    fun `dismiss progress snackbar`() {
        scenario.onActivity {
            // Given
            val snackbarFeature = ProgressSnackbarFeature(it)

            snackbarFeature.setVisibility(true)

            Thread.sleep(1000)

            // When
            snackbarFeature.setVisibility(false)

            // Then
            Espresso.onView(ViewMatchers.withId(com.google.android.material.R.id.snackbar_text))
                .check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
        }
    }
}
