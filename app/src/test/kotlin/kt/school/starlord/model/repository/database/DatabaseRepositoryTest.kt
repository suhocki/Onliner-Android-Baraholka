package kt.school.starlord.model.repository.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kt.school.starlord.di.mapperModule
import kt.school.starlord.entity.Category
import kt.school.starlord.model.data.mapper.Mapper
import kt.school.starlord.model.data.room.DaoManager
import kt.school.starlord.model.data.room.entity.RoomCategory
import kt.school.starlord.model.data.room.entity.RoomProduct
import kt.school.starlord.model.data.room.entity.RoomSubcategory
import kt.school.starlord.model.repository.mock.MockRepository
import kt.school.starlord.ui.TestCoroutineRule
import kt.school.starlord.ui.observeForTesting
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.test.AutoCloseKoinTest
import org.koin.test.inject
import org.mockito.ArgumentMatchers.anyString

@RunWith(AndroidJUnit4::class)
class DatabaseRepositoryTest : AutoCloseKoinTest() {
    @get:Rule
    internal val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    internal val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val mapper: Mapper by inject()
    private val daoManager: DaoManager = mockk(relaxed = true)
    private val mockRepository = MockRepository()

    private val roomRepository = DatabaseRepository(daoManager, mapper)

    @Before
    fun before() {
        unloadKoinModules(listOf(mapperModule))
        loadKoinModules(mapperModule)
    }

    @Test
    fun `get categories`() {
        // Given
        val roomData: List<RoomCategory> = listOf(
            RoomCategory("category1"),
            RoomCategory("category2"),
            RoomCategory("category3")
        )
        every { daoManager.categoryDao.getCategories() } returns MutableLiveData(roomData)

        // When
        val categories: LiveData<List<Category>> = roomRepository.getCategories()

        // Then
        categories.observeForTesting { answer ->
            assert(answer.all {
                val indexOfCategory = answer.indexOf(it)
                it.name == roomData[indexOfCategory].name
            })
        }
    }

    @Test
    fun `update categories`() = testCoroutineRule.runBlockingTest {
        // Given
        val categories = mockRepository.getCategories().value!!
        val roomCategories = slot<List<RoomCategory>>()
        coEvery { daoManager.categoryDao.replaceAll(capture(roomCategories)) } coAnswers { Unit }

        // When
        roomRepository.updateCategories(categories)

        // Then
        assert(roomCategories.isCaptured)
        assert(roomCategories.captured.all {
            val indexOfRoomCategory = roomCategories.captured.indexOf(it)

            it.name == categories[indexOfRoomCategory].name
        })
    }

    @Test
    fun `get subcategories`() {
        // Given
        val categoryName = "name"
        val roomData: List<RoomSubcategory> = listOf(
            RoomSubcategory("category1", "name", 1, "link1"),
            RoomSubcategory("category2", "name", 2, "link2"),
            RoomSubcategory("category3", "name", 3, "link3")
        )
        every { daoManager.subcategoryDao.getSubcategories(categoryName) }
            .returns(MutableLiveData(roomData))

        // When
        val subcategories = roomRepository.getSubcategories(categoryName)

        // Then
        subcategories.observeForTesting { answer ->
            assert(answer.all { subcategory ->
                val indexOfCategory = answer.indexOf(subcategory)
                subcategory.name == roomData[indexOfCategory].name &&
                        subcategory.link == roomData[indexOfCategory].link &&
                        subcategory.count == roomData[indexOfCategory].count &&
                        subcategory.categoryName == categoryName
            })
        }
    }

    @Test
    fun `update subcategories`() = testCoroutineRule.runBlockingTest {
        // Given
        val categoryName = "name"
        val subcategories = mockRepository.getSubcategories(categoryName).value!!
        val roomSubcategories = slot<List<RoomSubcategory>>()
        coEvery { daoManager.subcategoryDao.replaceAll(capture(roomSubcategories)) } coAnswers { Unit }

        // When
        roomRepository.updateSubcategories(subcategories)

        // Then
        assert(roomSubcategories.isCaptured)
        assert(roomSubcategories.captured.all { roomSubcategory ->
            val indexOfRoomSubcategory = roomSubcategories.captured.indexOf(roomSubcategory)
            val subcategory = subcategories[indexOfRoomSubcategory]

            roomSubcategory.name == subcategory.name &&
                    roomSubcategory.link == subcategory.link &&
                    roomSubcategory.count == subcategory.count &&
                    roomSubcategory.categoryName == categoryName
        })
    }

    @Test
    fun `get products`() {
        // Given
        val subcategoryName = anyString()
        val roomProducts = listOf(
            RoomProduct(1, subcategoryName, "", "", mockk(), "", "", mockk(), mockk(), "", 0, false),
            RoomProduct(2, subcategoryName, "", "", mockk(), "", "", mockk(), mockk(), "", 0, false)
        )

        every { daoManager.productDao.getProducts(subcategoryName) } returns MutableLiveData(roomProducts)

        // When
        val liveData = roomRepository.getProducts(subcategoryName)

        // Then
        liveData.observeForTesting {
            assert(it.size == 2)
            assert(it[0].id == 1L)
            assert(it[1].id == 2L)
        }
    }
}
