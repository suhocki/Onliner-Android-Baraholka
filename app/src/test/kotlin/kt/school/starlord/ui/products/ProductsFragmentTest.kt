package kt.school.starlord.ui.products

import android.os.Bundle
import androidx.fragment.app.testing.FragmentScenario
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.verify
import kotlinx.android.synthetic.main.fragment_products.*
import kt.school.starlord.domain.system.view.ErrorSnackbar
import kt.school.starlord.domain.system.view.ProgressSnackbar
import kt.school.starlord.entity.product.Product
import kt.school.starlord.entity.subcategory.Subcategory
import kt.school.starlord.model.repository.mock.MockRepository
import kt.school.starlord.ui.global.AppRecyclerAdapter
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.test.AutoCloseKoinTest
import org.koin.test.mock.declare
import org.mockito.ArgumentMatchers.anyString

@RunWith(AndroidJUnit4::class)
class ProductsFragmentTest : AutoCloseKoinTest() {

    private val viewModel: ProductsViewModel = mockk(relaxed = true)
    private val progressSnackbar: ProgressSnackbar = mockk(relaxUnitFun = true)
    private val errorSnackbar: ErrorSnackbar = mockk(relaxUnitFun = true)
    private val mockRepository = MockRepository()
    private val subcategory: Subcategory = mockk()
    private val arguments = Bundle().apply { putParcelable("subcategory", subcategory) }
    private val scenario by lazy {
        FragmentScenario.launchInContainer(ProductsFragment::class.java, arguments)
    }

    @Before
    fun setUp() {
        declare {
            viewModel { viewModel }
            single { errorSnackbar }
            single { progressSnackbar }
        }
    }

    // region Testing snackbars
    @Test
    fun `show snackbar with updating`() {
        // Given
        val progress = MutableLiveData(false)

        every { viewModel.getProgress() } returns progress

        scenario.onFragment {
            // When
            progress.value = true

            // Then
            verify { progressSnackbar.setVisibility(false) }
        }
    }

    @Test
    fun `hide snackbar with updating`() {
        // Given
        val progress = MutableLiveData<Boolean>()

        every { viewModel.getProgress() } returns progress

        scenario.onFragment {
            // When
            progress.value = false

            // Then
            verify { progressSnackbar.setVisibility(false) }
        }
    }

    @Test
    fun `hide snackbar with updating when fragment is gone`() {
        // Given
        val progressLiveData = MutableLiveData<Boolean>()

        every { viewModel.getProgress() } returns progressLiveData

        scenario.onFragment {
            // When
            progressLiveData.value = true

            scenario.moveToState(Lifecycle.State.DESTROYED)

            // Then
            verify { progressSnackbar.setVisibility(false) }
        }
    }

    @Test
    fun `show snackbar with error`() {
        // Given
        val error = Throwable(anyString())
        val errorLiveData = MutableLiveData<Throwable>()

        every { viewModel.getError() } returns errorLiveData

        scenario.onFragment {
            // When
            errorLiveData.value = error

            // Then
            verify { errorSnackbar.show(error) }
        }
    }

    @Test
    fun `hide snackbar with error when fragment is gone`() {
        // Given
        val error = Throwable(anyString())
        val errorLiveData = MutableLiveData<Throwable>()

        every { viewModel.getError() } returns errorLiveData

        scenario.onFragment {
            // When
            errorLiveData.value = error

            scenario.moveToState(Lifecycle.State.DESTROYED)

            // Then
            verify { errorSnackbar.dismiss() }
        }
    }
    // endregion

    @Test
    fun `show products`() {
        // Given
        mockkConstructor(AppRecyclerAdapter::class)
        val products: MutableLiveData<List<Product>> = mockRepository.getProductsLiveData(anyString())
        every { viewModel.getProducts() } returns products
        scenario.moveToState(Lifecycle.State.CREATED)

        // When
        scenario.moveToState(Lifecycle.State.RESUMED)

        // Then
        scenario.onFragment {
            verify { anyConstructed<AppRecyclerAdapter>().setData(products.value!!) }
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
