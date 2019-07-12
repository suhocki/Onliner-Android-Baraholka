package kt.school.starlord.ui.products

import android.Manifest
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onIdle
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import io.mockk.every
import io.mockk.mockk
import kt.school.starlord.AppActivity
import kt.school.starlord.R
import kt.school.starlord.entity.Category
import kt.school.starlord.entity.Subcategory
import kt.school.starlord.ui.categories.CategoriesViewModel
import kt.school.starlord.ui.categories.CategoryAdapterDelegate
import kt.school.starlord.ui.subcategories.SubcategoriesViewModel
import kt.school.starlord.ui.subcategories.SubcategoryAdapterDelegate
import kt.school.starlord.ui.takeScreenshot
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.test.KoinTest
import org.koin.test.mock.declare

class ProductsScreenshot : KoinTest {

    @get:Rule
    val permissionRule: GrantPermissionRule =
        GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE)

    @get:Rule
    val activityTestRule = object : ActivityTestRule<AppActivity>(AppActivity::class.java) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            injectMockedData()
        }
    }
    private val categoriesViewModel: CategoriesViewModel = mockk(relaxed = true)
    private val subcategoriesViewModel: SubcategoriesViewModel = mockk(relaxed = true)

    fun injectMockedData() {
        declare {
            viewModel { categoriesViewModel }
            viewModel { subcategoriesViewModel }
        }

        every { categoriesViewModel.getCategories() } returns MutableLiveData<List<Category>>(
            listOf(
                mockk(relaxed = true)
            )
        )
        every { subcategoriesViewModel.getSubcategories() } returns MutableLiveData<List<Subcategory>>(
            listOf(mockk(relaxed = true))
        )
    }

    @Before
    fun before() {
        onIdle()
    }

    @Test
    fun captureCategoriesScreen() {
        val clickOnFirstCategory = RecyclerViewActions
            .actionOnItemAtPosition<CategoryAdapterDelegate.ViewHolder>(0, ViewActions.click())
        val clickOnFirstSubcategory = RecyclerViewActions
            .actionOnItemAtPosition<SubcategoryAdapterDelegate.ViewHolder>(0, ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.recyclerView)).perform(clickOnFirstCategory)
        onIdle()
        Espresso.onView(ViewMatchers.withId(R.id.recyclerView)).perform(clickOnFirstSubcategory)
        onIdle()

        takeScreenshot("2_products")
    }
}
