package kt.school.starlord.ui.categories

import androidx.fragment.app.testing.FragmentScenario
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

    @Before
    fun setUp() {
        declare {
            viewModel { viewModel }
            single { systemMessageReceiver }
        }
    }

    @Test
    fun `load categories from database and network`() {
        // When
        val scenario = FragmentScenario.launchInContainer(CategoriesFragment::class.java)

        // Then
        scenario.onFragment {
            verify {
                viewModel.loadLocalCategories()
                viewModel.loadRemoteCategories()
            }
        }
    }

    @Test
    fun `show categories`() {
        // Given
        mockkConstructor(AppRecyclerAdapter::class)
        val categories = MutableLiveData<List<Category>>(MockedData.categories)
        every { viewModel.getCategories() } returns categories

        // When
        val scenario = FragmentScenario.launchInContainer(CategoriesFragment::class.java)

        // Then
        scenario.onFragment {
            verify { anyConstructed<AppRecyclerAdapter>().setData(MockedData.categories) }
        }
    }

    @Test
    fun `show progress`() {
        // Given
        val isProgressVisible = true
        every { viewModel.getProgress() } returns MutableLiveData<Boolean>(isProgressVisible)

        // When
        val scenario = FragmentScenario.launchInContainer(CategoriesFragment::class.java)

        // Then
        scenario.onFragment {
            verify { systemMessageReceiver.showProgress(isProgressVisible) }
        }
    }

    @Test
    fun `show error`() {
        // Given
        val exception = IllegalStateException("error")
        val error = MutableLiveData<Throwable>(exception)
        every { viewModel.getErrors() } returns error

        // When
        val scenario = FragmentScenario.launchInContainer(CategoriesFragment::class.java)

        // Then
        scenario.onFragment {
            verify { systemMessageReceiver.showError(exception) }
        }
    }

    @Test
    fun `navigate to subcategories`() {
        // Given
        val categoryName = "test category name"
        val categories = MutableLiveData<List<Category>>(listOf(Category(categoryName)))
        val navController: NavController = mockk(relaxUnitFun = true)
        mockkStatic(NavHostFragment::class)

        every { viewModel.getCategories() } returns categories
        every { NavHostFragment.findNavController(any()) } returns navController

        // When
        val scenario = FragmentScenario.launchInContainer(CategoriesFragment::class.java)

        // Then
        scenario.onFragment {
            onView(withText(categoryName)).perform(ViewActions.click())
            val direction = slot<NavDirections>()

            verify { navController.navigate(capture(direction)) }

            val arguments = direction.captured.arguments
            val keys = arguments.keySet()
            assert(keys.any { arguments.getString(it) == categoryName })
        }
    }
}
