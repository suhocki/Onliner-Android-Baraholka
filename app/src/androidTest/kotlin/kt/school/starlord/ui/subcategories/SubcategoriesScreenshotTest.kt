package kt.school.starlord.ui.subcategories

import android.Manifest
import androidx.test.espresso.Espresso.onIdle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import kt.school.starlord.AppActivity
import kt.school.starlord.R
import kt.school.starlord.di.module.databaseModule
import kt.school.starlord.di.module.mockModule
import kt.school.starlord.di.module.networkModule
import kt.school.starlord.ui.categories.CategoryAdapterDelegate
import kt.school.starlord.ui.takeScreenshot
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.test.KoinTest

class SubcategoriesScreenshotTest : KoinTest {

    @get:Rule
    val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE)

    @get:Rule
    val activityTestRule = object : ActivityTestRule<AppActivity>(AppActivity::class.java) {
        override fun beforeActivityLaunched() {
            unloadKoinModules(listOf(
                databaseModule,
                networkModule,
                mockModule
            ))
            loadKoinModules(mockModule)
        }
    }

    @Before
    fun before() {
        onIdle()
    }

    @Test
    fun captureSubcategoriesScreen() {
        val clickOnFirstItem = actionOnItemAtPosition<CategoryAdapterDelegate.ViewHolder>(0, click())
        onView(withId(R.id.recyclerView)).perform(clickOnFirstItem)

        onIdle()

        takeScreenshot("1_subcategories")
    }
}
