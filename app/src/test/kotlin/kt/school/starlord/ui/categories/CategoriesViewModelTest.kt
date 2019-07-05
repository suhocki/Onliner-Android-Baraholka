package kt.school.starlord.ui.categories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kt.school.starlord.entity.Category
import kt.school.starlord.entity.Subcategory
import kt.school.starlord.model.network.NetworkRepository
import kt.school.starlord.model.room.RoomRepository
import kt.school.starlord.ui.TestCoroutineRule
import kt.school.starlord.ui.observeForTesting
import org.junit.Rule
import org.junit.Test

class CategoriesViewModelTest {

    @get:Rule
    internal val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    internal val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val networkRepository: NetworkRepository = mockk()
    private val roomRepository: RoomRepository = mockk(relaxed = true)

    @Test
    fun `refresh data by network`() = testCoroutineRule.runBlockingTest {
        // Given
        val categoriesWithSubcategories = mapOf<Category, List<Subcategory>>(
            mockk<Category>() to listOf(mockk(), mockk()),
            mockk<Category>() to listOf(mockk(), mockk()),
            mockk<Category>() to listOf(mockk(), mockk())
        )
        coEvery { networkRepository.getCategoriesWithSubcategories() }.coAnswers { categoriesWithSubcategories }

        // When
        CategoriesViewModel(networkRepository, roomRepository)

        // Then
        coVerify(exactly = 1) {
            roomRepository.updateSubcategories(categoriesWithSubcategories.values.flatten())
            roomRepository.updateCategories(categoriesWithSubcategories.keys.toList())
        }
    }

    @Test
    fun `load data from database`() = testCoroutineRule.runBlockingTest {
        // Given
        val categories: List<Category> = mockk()
        val categoriesLiveData = MutableLiveData(categories)
        every { roomRepository.getCategories() }.answers { categoriesLiveData }

        // When
        val viewModel = CategoriesViewModel(networkRepository, roomRepository)

        // Then
        viewModel.getCategories().observeForTesting {
            assert(viewModel.getCategories().value == categories)
        }
    }
}
