package kt.school.starlord.ui.subcategories

import android.os.Bundle
import androidx.fragment.app.testing.FragmentScenario
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.verify
import kt.school.starlord.entity.Subcategory
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.test.AutoCloseKoinTest
import org.koin.test.mock.declare

@RunWith(AndroidJUnit4::class)
class SubcategoriesFragmentTest : AutoCloseKoinTest() {
    private val viewModel: SubcategoriesViewModel = mockk(relaxed = true)
    private val arguments = Bundle().apply { putString("categoryName", "some data") }
    private val scenario by lazy {
        FragmentScenario.launchInContainer(SubcategoriesFragment::class.java, arguments)
    }

    @Before
    fun setUp() {
        declare {
            viewModel { viewModel }
        }
    }

    @Test
    fun `navigate to products`() {
        // Given
        val subcategoryName = "subcategoryName"
        val subcategories = MutableLiveData(listOf(Subcategory(subcategoryName, "", 0, "")))
        val navController: NavController = mockk(relaxUnitFun = true)
        mockkStatic(NavHostFragment::class)

        every { viewModel.getSubcategories() } returns subcategories
        every { NavHostFragment.findNavController(any()) } returns navController

        // When
        scenario.onFragment {
            Espresso.onView(ViewMatchers.withText(subcategoryName)).perform(ViewActions.click())
        }

        // Then
        scenario.onFragment {
            val direction = slot<NavDirections>()

            verify { navController.navigate(capture(direction)) }

            val arguments = direction.captured.arguments
            val keys = arguments.keySet()
            assert(keys.any { arguments.getString(it) == subcategoryName })
        }
    }
}
