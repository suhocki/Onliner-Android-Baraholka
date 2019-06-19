package kt.school.starlord.ui.categories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kt.school.starlord.entity.Category
import kt.school.starlord.entity.Subcategory
import kt.school.starlord.model.network.NetworkRepository
import kt.school.starlord.model.room.RoomRepository
import org.junit.Rule
import org.junit.Test

internal class CategoriesViewModelTest {

    @get:Rule
    internal val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    internal val instantTaskExecutorRule = InstantTaskExecutorRule()

    private var networkRepository: NetworkRepository = mockk()
    private var roomRepository: RoomRepository = mockk(relaxUnitFun = true)

    private var viewModel = CategoriesViewModel(networkRepository, roomRepository)

    @Test
    fun `load categories with subcategories by network`() = testCoroutineRule.runBlockingTest {
        // Given: NetworkRepository returns mocked map of categories with subcategories
        val categoriesWithSubcategories = mapOf(
            Category("Телефоны. Смартфоны") to listOf(
                Subcategory("Мобильные телефоны", "Телефоны. Смартфоны", 7572, "link1"),
                Subcategory("Мобильные телефоны: аксессуары и запчасти", "Телефоны. Смартфоны", 7832, "link2")
            ),
            Category("Apple") to listOf(
                Subcategory("Ноутбуки", "Apple", 7572, "link1")
            )
        )
        coEvery { networkRepository.getCategoriesWithSubcategories() }.coAnswers { categoriesWithSubcategories }

        // When: loading categories by network
        viewModel.loadRemoteCategories()

        // Then
        val categories = categoriesWithSubcategories.keys.toList()
        coVerify(exactly = 1) { roomRepository.updateCategories(categories) }
        coVerify(exactly = 1) { roomRepository.updateSubcategories(categoriesWithSubcategories.values.flatten()) }
        viewModel.categoriesLiveData.observeForTesting {
            assert(viewModel.categoriesLiveData.value == categories)
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
        viewModel.categoriesLiveData.observeForTesting {
            assert(viewModel.categoriesLiveData.value == categories)
        }
    }
}