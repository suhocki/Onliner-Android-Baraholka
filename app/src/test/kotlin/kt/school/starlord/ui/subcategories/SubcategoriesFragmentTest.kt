package kt.school.starlord.ui.subcategories

import android.os.Bundle
import androidx.fragment.app.testing.FragmentScenario
import androidx.lifecycle.MutableLiveData
import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.*
import kt.school.starlord.R
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

    @Before
    fun setUp() {
        declare {
            viewModel { viewModel }
        }
    }

    @Test
    fun navigateToSubcategories() {
        // Given
        val subcategoryName = "subcategoryName"
        val subcategories = MutableLiveData(listOf(Subcategory(subcategoryName, "", 0, "")))
        val navController: NavController = mockk(relaxUnitFun = true)
        val arguments = Bundle().apply { putString("categoryName", "some data") }
        mockkStatic(NavHostFragment::class)

        every { viewModel.getSubcategories() } returns subcategories
        every { NavHostFragment.findNavController(any()) } returns navController

        // When
        val scenario = FragmentScenario.launchInContainer(SubcategoriesFragment::class.java, arguments)

        // Then
        scenario.onFragment {
            Espresso.onView(ViewMatchers.withText(subcategoryName)).perform(ViewActions.click())
            val direction = slot<ActionOnlyNavDirections>()

            verify { navController.navigate(capture(direction)) }

            assert(direction.captured.actionId == R.id.to_products)
        }
    }
}
