package kt.school.starlord.ui.categories

import androidx.fragment.app.testing.FragmentScenario
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.verify
import kt.school.starlord.entity.Category
import kt.school.starlord.model.system.SystemMessageReceiver
import kt.school.starlord.ui.global.AppRecyclerAdapter
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.test.AutoCloseKoinTest
import org.koin.test.mock.declare

@RunWith(AndroidJUnit4::class)
class CategoriesFragmentTest : AutoCloseKoinTest() {

    private val systemMessageReceiver: SystemMessageReceiver = mockk(relaxUnitFun = true)
    private val viewModel: CategoriesViewModel = mockk(relaxed = true)
    private val scenario by lazy {
        FragmentScenario.launchInContainer(CategoriesFragment::class.java)
    }

    @Before
    fun setUp() {
        declare {
            viewModel { viewModel }
            single { systemMessageReceiver }
        }
    }

    @Test
    fun loadCategoriesFromDatabaseAndNetwork() {
        // When
        scenario.moveToState(Lifecycle.State.CREATED)

        // Then
        scenario.onFragment {
            verify {
                viewModel.loadLocalCategories()
                viewModel.loadRemoteCategories()
            }
        }
    }

    @Test
    fun showCategories() {
        // Given
        mockkConstructor(AppRecyclerAdapter::class)
        val categories = listOf(Category("categoryName1"), Category("categoryName2"))
        every { viewModel.getCategories() } returns MutableLiveData<List<Category>>(categories)
        scenario.moveToState(Lifecycle.State.CREATED)

        // When
        scenario.moveToState(Lifecycle.State.RESUMED)

        // Then
        scenario.onFragment {
            verify { anyConstructed<AppRecyclerAdapter>().setData(categories) }
        }
    }

    @Test
    fun showProgress() {
        // Given
        val isProgressVisible = true
        every { viewModel.getProgress() } returns MutableLiveData<Boolean>(isProgressVisible)

        // Then
        scenario.onFragment {
            verify { systemMessageReceiver.showProgress(isProgressVisible) }
        }
    }

    @Test
    fun showError() {
        // Given
        val exception = IllegalStateException("error")
        val error = MutableLiveData<Throwable>(exception)
        every { viewModel.getErrors() } returns error

        // Then
        scenario.onFragment {
            verify { systemMessageReceiver.showError(exception) }
        }
    }

    @Test
    fun navigateToSubcategories() {
        // Given
        val categoryName = "test category name"
        val categories = MutableLiveData<List<Category>>(listOf(Category(categoryName)))
        val navController: NavController = mockk(relaxUnitFun = true)
        mockkStatic(NavHostFragment::class)
        every { viewModel.getCategories() } returns categories
        every { NavHostFragment.findNavController(any()) } returns navController

        // When
        scenario.onFragment {
            onView(withText(categoryName)).perform(ViewActions.click())
        }

        // Then
        scenario.onFragment {
            val direction = slot<NavDirections>()

            verify { navController.navigate(capture(direction)) }

            val arguments = direction.captured.arguments
            val keys = arguments.keySet()
            assert(keys.any { arguments.getString(it) == categoryName })
        }
    }
}
