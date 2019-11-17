package kt.school.starlord.ui.products

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.DataSource
import androidx.paging.PositionalDataSource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.every
import io.mockk.mockk
import kt.school.starlord.domain.data.mapper.BaseConverter
import kt.school.starlord.domain.data.mapper.Mapper
import kt.school.starlord.domain.entity.product.Product
import kt.school.starlord.domain.entity.subcategory.Subcategory
import kt.school.starlord.domain.repository.product.ProductsCacheRepository
import kt.school.starlord.domain.repository.product.ProductsRepository
import kt.school.starlord.model.repository.mock.MockRepository
import kt.school.starlord.model.system.viewmodel.ErrorViewModelFeature
import kt.school.starlord.model.system.viewmodel.ProgressViewModelFeature
import kt.school.starlord.ui.TestCoroutineRule
import kt.school.starlord.ui.global.entity.UiEntity
import kt.school.starlord.ui.observeForTesting
import org.junit.Rule
import org.junit.Test

/* ktlint-disable */
class ProductsViewModelTest {
    @get:Rule
    internal val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    internal val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val link = "https://baraholka.onliner.by/viewforum.php?f=2"
    private val subcategory = Subcategory("subcategoryName", 0, link)
    private val errorFeature: ErrorViewModelFeature = mockk(relaxUnitFun = true)
    private val progressFeature: ProgressViewModelFeature = mockk(relaxUnitFun = true)
    private val networkRepository: ProductsRepository = mockk()
    private val databaseRepository: ProductsCacheRepository = mockk(relaxed = true)

    @Test
    fun loadProducts_fromCache() = testCoroutineRule.runBlockingTest {
        // Given
        val products: List<Product> = listOf(mockk(), mockk(), mockk())
        val expected: Array<UiEntity> = arrayOf(mockk(), mockk(), mockk())

        every { databaseRepository.getCachedProducts(subcategory.name) }.answers { createDataSource(products) }

        val viewModel = createViewModel(
            mapper = Mapper(setOf(createConverter(products.zip(expected).toMap()))),
            subcategory = subcategory,
            databaseRepository = databaseRepository
        )

        // When
        val productsLiveData = viewModel.getProducts()

        // Then
        productsLiveData.observeForTesting { assert(expected.contentEquals(it.toTypedArray())) }
    }

    @Test
    fun loadProducts_fromNetwork_updateCache() = testCoroutineRule.runBlockingTest {
        // Given
        val fetchedProducts: List<Product> = listOf(mockk())

        coEvery { networkRepository.getProducts(link) }.coAnswers { fetchedProducts }

        // When
        createViewModel(
            subcategory = subcategory,
            databaseRepository = databaseRepository,
            networkRepository = networkRepository
        )

        // Then
        coVerify { databaseRepository.updateProducts(subcategory.name, fetchedProducts) }
    }

    @Test
    fun loadProducts_fromNetwork_showError() = testCoroutineRule.runBlockingTest {
        // Given
        val error = Throwable()

        coEvery { networkRepository.getProducts(any()) }.throws(error)

        // When
        createViewModel(errorFeature = errorFeature, networkRepository = networkRepository)

        // Then
        coVerify { errorFeature.showError(error) }
    }

    @Test
    fun loadProducts_fromNetwork_showProgress() = testCoroutineRule.runBlockingTest {
        // When
        createViewModel(progressFeature = progressFeature)

        // Then
        coVerifyOrder {
            progressFeature.showProgress(true)
            progressFeature.showProgress(false)
        }
    }

    private fun createViewModel(
        mapper: Mapper = mockk(relaxed = true),
        progressFeature: ProgressViewModelFeature = mockk(relaxed = true),
        errorFeature: ErrorViewModelFeature = mockk(relaxed = true),
        networkRepository: ProductsRepository = mockk(relaxed = true),
        databaseRepository: ProductsCacheRepository = mockk(relaxed = true),
        subcategory: Subcategory = mockk(relaxed = true)
    ) = ProductsViewModel(mapper, progressFeature, errorFeature, networkRepository, databaseRepository, subcategory)

    private fun <T> createDataSource(items: List<T>) = object : DataSource.Factory<Int, T>() {
        override fun create() = object : PositionalDataSource<T>() {
            override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<T>) =
                callback.onResult(items)

            override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<T>) =
                callback.onResult(items, 0, items.count())
        }
    }

    private inline fun <reified FROM : Any, reified TO : Any> createConverter(map: Map<FROM, TO>) =
        object : BaseConverter<FROM, TO>(FROM::class, TO::class) {
            override fun convert(value: FROM) = map[value] ?: error("Could not convert $value.")
        }
}
