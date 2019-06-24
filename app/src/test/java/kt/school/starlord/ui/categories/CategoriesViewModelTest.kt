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
    fun `load categories with subcategories by network`() = testCoroutineRule.runBlockingTest {
        // Given: NetworkRepository returns mocked map of categories with subcategories
        val categoriesWithSubcategories = mapOf(
            Category("categoryName1") to listOf(
                Subcategory("subcategoryName1", "categoryName1", 5, "link1"),
                Subcategory("subcategoryName2", "categoryName1", 2, "link2")
            ),
            Category("categoryName2") to listOf(
                Subcategory("subcategoryName3", "categoryName2", 3, "link3")
            )
        )
        coEvery { networkRepository.getCategoriesWithSubcategories() }.coAnswers { categoriesWithSubcategories }

        // When: loading categories by network
        viewModel.loadRemoteCategories()

        // Then
        val categories = categoriesWithSubcategories.keys.toList()
        coVerify(exactly = 1) { roomRepository.updateCategories(categories) }
        coVerify(exactly = 1) { roomRepository.updateSubcategories(categoriesWithSubcategories.values.flatten()) }
        viewModel.categories.observeForTesting {
            assert(viewModel.categories.value == categories)
        }
    }

    @Test
    fun `load categories from database`() = testCoroutineRule.runBlockingTest {
        // Given: RoomRepository returns mocked categories
        val categories: List<Category> = mockk()
        coEvery { roomRepository.getCategories() }.coAnswers { categories }

        // When: loading categories from database
        viewModel.loadLocalCategories()

        // Then
        viewModel.categories.observeForTesting {
            assert(viewModel.categories.value == categories)
        }
    }
}
