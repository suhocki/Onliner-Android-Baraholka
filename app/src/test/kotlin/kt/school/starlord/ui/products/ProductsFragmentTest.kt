package kt.school.starlord.ui.products

import android.os.Bundle
import android.view.View
import androidx.fragment.app.testing.FragmentScenario
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.hadilq.liveevent.LiveEvent
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkStatic
import io.mockk.verify
import kotlinx.android.synthetic.main.fragment_products.*
import kt.school.starlord.entity.product.Product
import kt.school.starlord.extension.showError
import kt.school.starlord.model.repository.mock.MockRepository
import kt.school.starlord.ui.global.AppRecyclerAdapter
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.test.AutoCloseKoinTest
import org.koin.test.mock.declare

@RunWith(AndroidJUnit4::class)
class ProductsFragmentTest : AutoCloseKoinTest() {

    private val viewModel: ProductsViewModel = mockk(relaxed = true)
    private val mockRepository = MockRepository()
    private val arguments = Bundle().apply { putString("subcategoryName", "some data") }
    private val scenario by lazy {
        FragmentScenario.launchInContainer(ProductsFragment::class.java, arguments)
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
        val errorLiveEvent = LiveEvent<Throwable>()

        every { viewModel.getError() } returns errorLiveEvent

        mockkStatic("kt.school.starlord.extension.AndroidExtensionsKt")

        scenario.onFragment {
            // When
            errorLiveEvent.value = error

            // Then
            verify { it.requireContext().showError(error) }
        }
    }

    @Test
    fun `show products`() {
        // Given
        mockkConstructor(AppRecyclerAdapter::class)
        val products: MutableLiveData<List<Product>> = mockRepository.getProducts("")
        every { viewModel.getProducts() } returns products
        scenario.moveToState(Lifecycle.State.CREATED)

        // When
        scenario.moveToState(Lifecycle.State.RESUMED)

        // Then
        scenario.onFragment {
            verify { anyConstructed<AppRecyclerAdapter>().setData(products.value!!) }
        }
    }
}
