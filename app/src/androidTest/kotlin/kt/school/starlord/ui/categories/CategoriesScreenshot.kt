package kt.school.starlord.ui.categories

import android.Manifest
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onIdle
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import io.mockk.every
import io.mockk.mockk
import kt.school.starlord.AppActivity
import kt.school.starlord.entity.Category
import kt.school.starlord.ui.takeScreenshot
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.test.KoinTest
import org.koin.test.mock.declare

class CategoriesScreenshot : KoinTest {

    @get:Rule
    val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE)

    @get:Rule
    val activityTestRule = object : ActivityTestRule<AppActivity>(AppActivity::class.java) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            injectMockedData()
        }
    }
    private val viewModel: CategoriesViewModel = mockk(relaxed = true)

    fun injectMockedData() {
        declare {
            viewModel { viewModel }
        }

        every { viewModel.getCategories() } returns MutableLiveData<List<Category>>(
            listOf(
                Category("Мобильные телефоны"),
                Category("Компьютеры"),
                Category("Фотоаппараты"),
                Category("Авто"),
                Category("Запчасти"),
                Category("Дом"),
                Category("Строительство"),
                Category("Семья"),
                Category("Работа"),
                Category("Животные"),
                Category("Праздники"),
                Category("Недвижимость"),
                Category("Женская одежда"),
                Category("Мужская одежда")
            )
        )
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
