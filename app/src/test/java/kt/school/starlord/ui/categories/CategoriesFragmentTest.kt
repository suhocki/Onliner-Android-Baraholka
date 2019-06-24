package kt.school.starlord.ui.categories

import androidx.fragment.app.testing.FragmentScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.mockk
import io.mockk.verify
import kt.school.starlord.model.network.NetworkRepository
import kt.school.starlord.model.room.RoomRepository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.test.AutoCloseKoinTest
import org.koin.test.mock.declare

@RunWith(AndroidJUnit4::class)
class CategoriesFragmentTest : AutoCloseKoinTest() {

    private val networkRepository: NetworkRepository = mockk()
    private val roomRepository: RoomRepository = mockk()

    private val viewModel = CategoriesViewModel(networkRepository, roomRepository)

    @Before
    fun setUp() {
        declare {
            viewModel { viewModel }
        }
    }

    @Test
    fun test() {
        // When
        val scenario = FragmentScenario.launchInContainer(CategoriesFragment::class.java)

        // Then
        scenario.onFragment {
            verify { viewModel.loadLocalCategories() }
        }
    }
}
