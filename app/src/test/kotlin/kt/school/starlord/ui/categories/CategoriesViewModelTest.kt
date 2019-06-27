package kt.school.starlord.ui.categories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kt.school.starlord.entity.Category
import kt.school.starlord.entity.Subcategory
import kt.school.starlord.model.network.NetworkRepository
import kt.school.starlord.model.room.RoomRepository
import kt.school.starlord.ui.TestCoroutineRule
import kt.school.starlord.ui.observeForTesting
import org.junit.Rule
import org.junit.Test

internal class CategoriesViewModelTest {

    @get:Rule
    internal val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    internal val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val networkRepository: NetworkRepository = mockk()
    private val roomRepository: RoomRepository = mockk(relaxUnitFun = true)

    private val viewModel = CategoriesViewModel(networkRepository, roomRepository)

    @Test
    fun `load categories with subcategories from network`() = testCoroutineRule.runBlockingTest {
        // Given
        val categoriesWithSubcategories = mapOf(
            Category("categoryName3") to listOf(
                Subcategory("subcategoryName1", "categoryName3", 5, "link1"),
                Subcategory("subcategoryName2", "categoryName3", 2, "link2")
            ),
            Category("categoryName4") to listOf(
                Subcategory("subcategoryName3", "categoryName4", 3, "link3")
            )
        )
        coEvery { networkRepository.getCategoriesWithSubcategories() }.coAnswers { categoriesWithSubcategories }

        // When
        viewModel.loadRemoteCategories()

        // Then
        val categories = categoriesWithSubcategories.keys.toList()
        coVerify(exactly = 1) {
            roomRepository.updateSubcategories(categoriesWithSubcategories.values.flatten())
            roomRepository.updateCategories(categories)
        }
        viewModel.getCategories().observeForTesting {
            assert(viewModel.getCategories().value == categories)
        }
    }

    @Test
    fun `load categories from database`() = testCoroutineRule.runBlockingTest {
        // Given
        val categories = listOf(Category("categoryName1"), Category("categoryName2"))
        coEvery { roomRepository.getCategories() }.coAnswers { categories }

        // When
        viewModel.loadLocalCategories()

        // Then
        viewModel.getCategories().observeForTesting {
            assert(viewModel.getCategories().value == categories)
        }
    }
}
