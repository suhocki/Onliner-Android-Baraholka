package kt.school.starlord

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import kt.school.starlord.ui.global.BaseActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppActivityTest : BaseActivity() {
    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(AppActivity::class.java)

    @Test
    fun testEvent() {
        onView(withId(R.id.main_nav_host_fragment)).check(matches(isDisplayed()))
    }

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    @Test
    fun onCreate() {
    }

    @Test
    public override fun onDestroy() {
    }

    @Test
    fun fixMemoryLeak() {
    }
}
