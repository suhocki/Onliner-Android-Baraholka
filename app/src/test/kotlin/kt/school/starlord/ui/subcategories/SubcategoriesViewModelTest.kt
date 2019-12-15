package kt.school.starlord.ui.subcategories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import io.mockk.every
import io.mockk.mockk
import kt.school.starlord.domain.mapper.Mapper
import kt.school.starlord.domain.entity.subcategory.Subcategory
import kt.school.starlord.domain.repository.category.SubcategoriesCacheRepository
import kt.school.starlord.ui.TestCoroutineRule
import kt.school.starlord.ui.createConverter
import kt.school.starlord.ui.observeForTesting
import kt.school.starlord.ui.subcategories.entity.UiSubcategory
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString

class SubcategoriesViewModelTest {

    @get:Rule
    internal val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    internal val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val subcategoriesRepository: SubcategoriesCacheRepository = mockk()

    @Test
    fun loadSubcategories_fromCache() = testCoroutineRule.runBlockingTest {
        // Given
        val categoryName = anyString()
        val subcategory: Subcategory = mockk()
        val uiSubcategory: UiSubcategory = mockk()
        val subcategories: List<Subcategory> = listOf(subcategory)
        val expected: List<UiSubcategory> = listOf(uiSubcategory)

        every { subcategoriesRepository.getSubcategories(categoryName) } answers { MutableLiveData(subcategories) }

        // When
        val viewModel = SubcategoriesViewModel(
            subcategoriesRepository,
            Mapper(setOf(createConverter(subcategory to uiSubcategory))),
            categoryName
        )

        // Then
        viewModel.getSubcategories().observeForTesting { assert(it == expected) }
    }
}
