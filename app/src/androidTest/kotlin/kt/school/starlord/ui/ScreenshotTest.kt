package kt.school.starlord.ui

import android.Manifest
import androidx.test.espresso.Espresso.onIdle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import kt.school.starlord.R
import kt.school.starlord.StarlordActivity
import kt.school.starlord.di.module.networkModule
import kt.school.starlord.domain.repository.category.CategoriesNetworkRepository
import kt.school.starlord.domain.repository.product.ProductsNetworkRepository
import kt.school.starlord.ui.categories.CategoryAdapterDelegate
import kt.school.starlord.ui.subcategories.SubcategoryAdapterDelegate
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.test.KoinTest

class ScreenshotTest : KoinTest {
    private val assets by lazy { InstrumentationRegistry.getInstrumentation().context.assets }

    private val categoriesRepository by lazy {
        object : CategoriesNetworkRepository {
            override suspend fun downloadCategoriesPage(): String =
                assets.open("html/categories.html.test").readBytes().toString(Charsets.UTF_8)
        }
    }

    private val productsRepository by lazy {
        object : ProductsNetworkRepository {
            override suspend fun downloadProductsPage(forumId: Long) =
                assets.open("html/products.html.test").readBytes().toString(Charsets.UTF_8)
        }
    }

    private val testNetworkModule by lazy {
        module {
            single { categoriesRepository } bind CategoriesNetworkRepository::class
            single { productsRepository } bind ProductsNetworkRepository::class
        }
    }

    @get:Rule
    val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE)

    @get:Rule
    val activityTestRule = object : ActivityTestRule<StarlordActivity>(StarlordActivity::class.java) {
        override fun beforeActivityLaunched() {
            unloadKoinModules(listOf(networkModule, testNetworkModule))
            loadKoinModules(testNetworkModule)
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

    @Test
    fun captureSubcategoriesScreen() {
        val clickOnFirstItem =
            RecyclerViewActions.actionOnItemAtPosition<CategoryAdapterDelegate.ViewHolder>(0, ViewActions.click())
        onView(ViewMatchers.withId(R.id.recyclerView)).perform(clickOnFirstItem)

        onIdle()

        takeScreenshot("1_subcategories")
    }

    @Test
    fun captureProductsScreen() {
        val clickOnFirstCategory = RecyclerViewActions
            .actionOnItemAtPosition<CategoryAdapterDelegate.ViewHolder>(0, ViewActions.click())
        val clickOnFirstSubcategory = RecyclerViewActions
            .actionOnItemAtPosition<SubcategoryAdapterDelegate.ViewHolder>(0, ViewActions.click())

        onView(ViewMatchers.withId(R.id.recyclerView)).perform(clickOnFirstCategory)
        onIdle()
        onView(ViewMatchers.withId(R.id.recyclerView)).perform(clickOnFirstSubcategory)
        onIdle()

        Thread.sleep(1000)

        takeScreenshot("2_products")
    }
}
