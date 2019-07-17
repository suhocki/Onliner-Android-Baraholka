package kt.school.starlord.ui.categories

import android.view.View
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
import kotlinx.android.synthetic.main.fragment_categories.*
import kt.school.starlord.entity.Category
import kt.school.starlord.extension.showError
import kt.school.starlord.ui.global.AppRecyclerAdapter
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.test.AutoCloseKoinTest
import org.koin.test.mock.declare

@RunWith(AndroidJUnit4::class)
class CategoriesFragmentTest : AutoCloseKoinTest() {

    private val viewModel: CategoriesViewModel = mockk(relaxed = true)
    private val scenario by lazy {
        FragmentScenario.launchInContainer(CategoriesFragment::class.java)
    }

    @Before
    fun setUp() {
        declare {
            viewModel { viewModel }
        }
    }

    @Test
    fun `show progress bar`() {
        // Given
        val progress = MutableLiveData(false)

        every { viewModel.getProgress() } returns progress

        scenario.onFragment {
            // When
            progress.value = true

            // Then
            assert(it.progressBar.visibility == View.VISIBLE)
        }
    }

    @Test
    fun `hide progress bar`() {
        // Given
        val progress = MutableLiveData<Boolean>()

        every { viewModel.getProgress() } returns progress

        scenario.onFragment {
            // When
            progress.value = false

            // Then
            assert(it.progressBar.visibility == View.GONE)
        }
    }

    @Test
    fun `show error message`() {
        // Given
        val error = Throwable("some error occured")
        val errorLiveData = MutableLiveData<Throwable>()

        every { viewModel.getError() } returns errorLiveData

        mockkStatic("kt.school.starlord.extension.AndroidExtensionsKt")

        scenario.onFragment {
            // When
            errorLiveData.value = error

            // Then
            verify { it.requireContext().showError(error) }
        }
    }

    @Test
    fun `show categories`() {
        // Given
        mockkConstructor(AppRecyclerAdapter::class)
        val categories = listOf(Category("categoryName1"), Category("categoryName2"))
        every { viewModel.getCategories() } returns MutableLiveData(categories)
        scenario.moveToState(Lifecycle.State.CREATED)

        // When
        scenario.moveToState(Lifecycle.State.RESUMED)

        // Then
        scenario.onFragment {
            verify { anyConstructed<AppRecyclerAdapter>().setData(categories) }
        }
    }

    @Test
    fun `navigate to subcategories`() {
        // Given
        val categoryName = "test category name"
        val categories = MutableLiveData(listOf(Category(categoryName)))
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

    @Test
    fun `clear adapter in recycler in onDestroy`() {
        // Given
        scenario.moveToState(Lifecycle.State.RESUMED)

        scenario.onFragment {
            val recyclerView = it.recyclerView

            // When
            scenario.moveToState(Lifecycle.State.DESTROYED)

            // Then
            assert(recyclerView.adapter == null)
        }
    }
}
