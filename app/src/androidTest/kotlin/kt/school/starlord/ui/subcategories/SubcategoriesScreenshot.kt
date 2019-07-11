package kt.school.starlord.ui.subcategories

import android.Manifest
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onIdle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.withId
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
import kt.school.starlord.ui.takeScreenshot
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.test.KoinTest
import org.koin.test.mock.declare

class SubcategoriesScreenshot : KoinTest {

    @get:Rule
    val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE)

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

        every { categoriesViewModel.getCategories() } returns MutableLiveData<List<Category>>(listOf(Category("")))
        every { subcategoriesViewModel.getSubcategories() } returns MutableLiveData<List<Subcategory>>(
            listOf(
                Subcategory("Шкафы. Комоды. Горки. Секции. Полки", "", 3388, ""),
                Subcategory("Диваны. Кресла. Мягкая мебель", "", 8962, ""),
                Subcategory("Столы. Стулья. Тумбы", "", 2575, ""),
                Subcategory("Кровати. Матрасы. Мебель для спальни", "", 2740, ""),
                Subcategory("Кухни и кухонная мебель", "", 2976, ""),
                Subcategory("Мебель для детской комнаты", "", 4059, ""),
                Subcategory("Мебель для ванной", "", 1904, ""),
                Subcategory("Офисная мебель", "", 4704, ""),
                Subcategory("Элементы интерьера. Дизайн.", "", 17044, ""),
                Subcategory("Постельное белье и принадлежности", "", 11112, ""),
                Subcategory("Посуда и кухонные принадлежности", "", 638, ""),
                Subcategory("Бытовая техника.", "", 2842, ""),
                Subcategory("Бытовая техника: ремонт, подключение и другие услуги", "", 423, "")
            )
        )
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

        takeScreenshot("subcategories")
    }
}
