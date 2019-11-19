package kt.school.starlord.model.repository.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.verify
import kt.school.starlord.domain.data.mapper.Mapper
import kt.school.starlord.domain.entity.category.Category
import kt.school.starlord.domain.entity.global.RussianLocalizedTimePassed
import kt.school.starlord.domain.entity.product.Product
import kt.school.starlord.domain.entity.subcategory.Subcategory
import kt.school.starlord.model.data.room.DaoManager
import kt.school.starlord.model.data.room.entity.RoomCategory
import kt.school.starlord.model.data.room.entity.RoomProduct
import kt.school.starlord.model.data.room.entity.RoomSubcategory
import kt.school.starlord.ui.TestCoroutineRule
import kt.school.starlord.ui.createConverter
import kt.school.starlord.ui.createDataSource
import kt.school.starlord.ui.global.entity.wrapper.LocalizedTimePassed
import kt.school.starlord.ui.observeForTesting
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.*
import org.threeten.bp.Instant

class DatabaseRepositoryTest {
    @get:Rule
    internal val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    internal val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val daoManager: DaoManager = mockk(relaxed = true)
    private val now = Instant.now()

    @Before
    fun setUp() {
        mockkStatic(Instant::class)

        every { Instant.now() } returns now
    }

    @Test
    fun getCategories() {
        // Given
        val roomCategories: List<RoomCategory> = listOf(mockk(), mockk(), mockk())
        val expected: Array<Category> = arrayOf(mockk(), mockk(), mockk())

        every { daoManager.categoryDao.getCategories() } returns MutableLiveData(roomCategories)

        val repository = createRepository(mapper = Mapper(setOf(createConverter(roomCategories.zip(expected).toMap()))))

        // When
        val categories: LiveData<List<Category>> = repository.getCategories()

        // Then
        categories.observeForTesting { assert(expected.contentEquals(it.toTypedArray())) }
    }

    @Test
    fun updateCategories() = testCoroutineRule.runBlockingTest {
        // Given
        val categories: List<Category> = listOf(mockk(), mockk(), mockk())
        val expected: Array<RoomCategory> = arrayOf(mockk(), mockk(), mockk())
        val roomCategories = slot<List<RoomCategory>>()
        val mapper = Mapper(setOf(createConverter(categories.zip(expected).toMap())))

        coEvery { daoManager.categoryDao.replaceAll(capture(roomCategories)) } coAnswers { Unit }

        // When
        createRepository(mapper = mapper).updateCategories(categories)

        // Then
        assert(roomCategories.captured.toTypedArray().contentEquals(expected))
    }

    @Test
    fun getSubcategories() {
        // Given
        val categoryName = "name"
        val roomSubcategories: List<RoomSubcategory> = listOf(mockk(), mockk(), mockk())
        val expected: Array<Subcategory> = arrayOf(mockk(), mockk(), mockk())
        val mapper = Mapper(setOf(createConverter(roomSubcategories.zip(expected).toMap())))

        every { daoManager.subcategoryDao.getSubcategories(categoryName) }.returns(MutableLiveData(roomSubcategories))

        // When
        val subcategories = createRepository(mapper = mapper).getSubcategories(categoryName)

        // Then
        subcategories.observeForTesting { assert(it.toTypedArray().contentEquals(expected)) }
    }

    @Test
    fun updateSubcategories() = testCoroutineRule.runBlockingTest {
        // Given
        val subcategories: List<Subcategory> = listOf(mockk(), mockk(), mockk())
        val expected: Array<RoomSubcategory> = arrayOf(mockk(), mockk(), mockk())
        val roomSubcategories = slot<List<RoomSubcategory>>()
        val mapper = Mapper(setOf(createConverter(subcategories.zip(expected).toMap())))

        coEvery { daoManager.subcategoryDao.replaceAll(capture(roomSubcategories)) } coAnswers { Unit }

        // When
        createRepository(mapper = mapper).updateSubcategories(subcategories)

        // Then
        assert(roomSubcategories.captured.toTypedArray().contentEquals(expected))
    }

    @Test
    fun getCachedProducts() {
        // Given
        val subcategoryName = anyString()
        val roomProducts: List<RoomProduct> = listOf(mockk(), mockk(), mockk())
        val expected: Array<Product> = arrayOf(mockk(), mockk(), mockk())
        val mapper = Mapper(setOf(createConverter(roomProducts.zip(expected).toMap())))

        every { daoManager.productDao.getProducts(subcategoryName) } returns createDataSource(roomProducts)

        // When
        val dataSourceFactory = createRepository(mapper = mapper).getCachedProducts(subcategoryName)

        // Then
        LivePagedListBuilder(dataSourceFactory, roomProducts.size).build().observeForTesting {
            it.toTypedArray().contentEquals(expected)
        }
    }

    @Test
    fun updateProducts_noCached() = testCoroutineRule.runBlockingTest {
        // Given
        val subcategoryName = anyString()
        val serverProducts: List<Product> = listOf(createProduct(1, 1, LocalizedTimePassed("1 hour ago")))
        val expected: Array<RoomProduct> = arrayOf(mockk())
        val actual = slot<List<RoomProduct>>()
        val mapper = Mapper(setOf(createConverter(serverProducts.zip(expected).toMap())))

        coEvery { daoManager.productDao.getProductsByIds(listOf(1), subcategoryName) } coAnswers { emptyList() }
        coEvery { daoManager.productDao.insertProducts(capture(actual)) } coAnswers { Unit }

        // When
        createRepository(mapper = mapper).updateProducts(subcategoryName, serverProducts)

        // Then
        assert(actual.captured.toTypedArray().contentEquals(expected))
    }

    @Test
    fun updateProducts_keepServerLastUpdate() =
        testCoroutineRule.runBlockingTest {
            // Given
            val (mapper, serverProduct) = createUpdateProductsMocks(
                100L to "5 минут назад",
                50L to "50 минут назад"
            )

            // When
            createRepository(mapper = mapper).updateProducts(anyString(), listOf(serverProduct))

            // Then
            verify(exactly = 0) { serverProduct.lastUpdate = 50L }
        }

    @Test
    fun updateProducts_applyCachedLastUpdate() = testCoroutineRule.runBlockingTest {
        // Given
        val (mapper, serverProduct) = createUpdateProductsMocks(
            50L to "10 минут назад",
            100L to "10 минут назад"
        )

        // When
        createRepository(mapper = mapper).updateProducts(anyString(), listOf(serverProduct))

        // Then
        verify(exactly = 1) { serverProduct.lastUpdate = 100L }
    }

    private fun createRepository(
        daoManager: DaoManager = this.daoManager,
        mapper: Mapper = mockk(relaxed = true)
    ) = DatabaseRepository(daoManager, mapper)

    private fun createProduct(
        id: Long = anyLong(),
        lastUpdate: Long = anyLong(),
        localizedTimePassed: LocalizedTimePassed = mockk()
    ) = mockk<Product>(relaxed = true).apply {
        every<Long> { this@apply.id } returns id
        every<Long> { this@apply.lastUpdate } returns lastUpdate
        every<LocalizedTimePassed> { this@apply.localizedTimePassed } returns localizedTimePassed
    }

    private fun createRoomProduct(
        id: Long = anyLong(),
        lastUpdate: Long = anyLong()
    ) = mockk<RoomProduct>(relaxed = true).apply {
        every<Long> { this@apply.id } returns id
        every<Long> { this@apply.lastUpdate } returns lastUpdate
    }

    private fun createUpdateProductsMocks(
        serverLastUpdateToServerLocalized: Pair<Long, String>,
        cachedLastUpdateToCachedLocalized: Pair<Long, String>
    ): Pair<Mapper, Product> {
        val productId = 1L
        val (serverLastUpdate, serverLocalized) = serverLastUpdateToServerLocalized
        val (cachedLastUpdate, cachedLocalized) = cachedLastUpdateToCachedLocalized

        val serverProduct = createProduct(productId, serverLastUpdate, LocalizedTimePassed(serverLocalized))

        coEvery { daoManager.productDao.getProductsByIds(listOf(productId), anyString()) } coAnswers {
            listOf(createRoomProduct(productId, cachedLastUpdate))
        }

        return Mapper(
            setOf(
                createConverter(mapOf(serverProduct to createRoomProduct(productId, serverLastUpdate))),
                createConverter(mapOf(now.toEpochMilli() - cachedLastUpdate to RussianLocalizedTimePassed(cachedLocalized)))
            )
        ) to serverProduct
    }

}
