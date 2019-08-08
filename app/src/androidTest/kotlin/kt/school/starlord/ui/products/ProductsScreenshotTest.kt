package kt.school.starlord.ui.products

import android.Manifest
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onIdle
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import kt.school.starlord.AppActivity
import kt.school.starlord.R
import kt.school.starlord.di.databaseModule
import kt.school.starlord.di.mockModule
import kt.school.starlord.di.networkModule
import kt.school.starlord.ui.categories.CategoryAdapterDelegate
import kt.school.starlord.ui.subcategories.SubcategoryAdapterDelegate
import kt.school.starlord.ui.takeScreenshot
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.test.KoinTest

class ProductsScreenshotTest : KoinTest {

    @get:Rule
    val permissionRule: GrantPermissionRule =
        GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE)

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
    fun captureProductsScreen() {
        val clickOnFirstCategory = RecyclerViewActions
            .actionOnItemAtPosition<CategoryAdapterDelegate.ViewHolder>(0, ViewActions.click())
        val clickOnFirstSubcategory = RecyclerViewActions
            .actionOnItemAtPosition<SubcategoryAdapterDelegate.ViewHolder>(0, ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.recyclerView)).perform(clickOnFirstCategory)
        onIdle()
        Espresso.onView(ViewMatchers.withId(R.id.recyclerView)).perform(clickOnFirstSubcategory)
        onIdle()

        Thread.sleep(1000)

        takeScreenshot("2_products")
    }
}
