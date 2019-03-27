package kt.school.starlord.ui.categories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kt.school.starlord.domain.CategoriesRepository
import kt.school.starlord.entity.Category
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.Executors


@ExperimentalCoroutinesApi
class CategoriesViewModelTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private val mainThreadSurrogate = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

    private var categoriesRepository: CategoriesRepository = mockk()

    private var viewModel = CategoriesViewModel(
        categoriesRepository,
        CoroutineScope(Dispatchers.Main)
    )

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @Test
    fun loadCategories() {
        // Given that the CategoriesRepository returns the list of categories
        val categories: List<Category> = mockk()
        every { categoriesRepository.getCategories() }.returns(categories)

        val observer = mockk<Observer<List<Category>>>(relaxUnitFun = true)
        viewModel.categories.observeForever(observer)

        // When loading categories
        viewModel.loadCategories()

        // The correct list of categories is emitted
        verify(exactly = 1) { observer.onChanged(categories) }
        verify(atMost = 1) { categoriesRepository.getCategories() }
    }
}