package kt.school.starlord.ui.categories

import android.Manifest
import androidx.test.espresso.Espresso.onIdle
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import kt.school.starlord.AppActivity
import kt.school.starlord.di.databaseModule
import kt.school.starlord.di.mockModule
import kt.school.starlord.di.networkModule
import kt.school.starlord.ui.takeScreenshot
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.test.KoinTest

class CategoriesScreenshotTest : KoinTest {

    @get:Rule
    val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE)

    @get:Rule
    val activityTestRule = object : ActivityTestRule<AppActivity>(AppActivity::class.java) {
        override fun beforeActivityLaunched() {
            unloadKoinModules(listOf(databaseModule, networkModule, mockModule))
            loadKoinModules(mockModule)
        }
    }

    @Before
    fun before() {
        onIdle()
    }

    @Test
    fun captureCategoriesScreen() {
        takeScreenshot("0_categories")
    }
}
