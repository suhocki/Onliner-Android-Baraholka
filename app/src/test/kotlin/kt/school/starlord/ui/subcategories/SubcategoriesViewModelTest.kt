package kt.school.starlord.ui.subcategories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kt.school.starlord.domain.repository.SubcategoriesRepository
import kt.school.starlord.domain.entity.subcategory.Subcategory
import kt.school.starlord.ui.TestCoroutineRule
import kt.school.starlord.ui.observeForTesting
import org.junit.Rule
import org.junit.Test

class SubcategoriesViewModelTest {

    @get:Rule
    internal val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    internal val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val subcategoriesRepository: SubcategoriesRepository = mockk()

    @Test
    fun loadSubcategories_fromCache() = testCoroutineRule.runBlockingTest {
        // Given
        val categoryName = "categoryName"
        val subcategories: List<Subcategory> = mockk()

        every { subcategoriesRepository.getSubcategories(categoryName) } answers { MutableLiveData(subcategories) }

        // When
        val viewModel = SubcategoriesViewModel(subcategoriesRepository, categoryName)

        // Then
        viewModel.getSubcategories().observeForTesting { assert(it == subcategories) }
    }
}
