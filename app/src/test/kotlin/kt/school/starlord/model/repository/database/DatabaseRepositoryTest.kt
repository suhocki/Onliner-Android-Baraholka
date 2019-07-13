package kt.school.starlord.model.repository.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kt.school.starlord.di.converters
import kt.school.starlord.entity.Category
import kt.school.starlord.model.data.room.DaoManager
import kt.school.starlord.model.data.mapper.Mapper
import kt.school.starlord.model.repository.mock.MockRepository
import kt.school.starlord.model.data.room.entity.RoomCategory
import kt.school.starlord.model.data.room.entity.RoomSubcategory
import kt.school.starlord.ui.TestCoroutineRule
import kt.school.starlord.ui.observeForTesting
import org.junit.Rule
import org.junit.Test

class DatabaseRepositoryTest {
    @get:Rule
    internal val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    internal val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val daoManager: DaoManager = mockk(relaxed = true)
    private val mapper = Mapper(converters)
    private val mockRepository = MockRepository()

    private val roomRepository = DatabaseRepository(daoManager, mapper)

    @Test
    fun `get categories`() {
        // Given
        val roomData: List<RoomCategory> = listOf(
            RoomCategory("category1"),
            RoomCategory("category2"),
            RoomCategory("category3")
        )
        every { daoManager.categoryDao.getCategories() } returns MutableLiveData<List<RoomCategory>>(roomData)

        // When
        val categories: LiveData<List<Category>> = roomRepository.getCategories()

        // Then
        categories.observeForTesting {
            val answer = categories.value!!
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
            .returns(MutableLiveData<List<RoomSubcategory>>(roomData))

        // When
        val subcategories = roomRepository.getSubcategories(categoryName)

        // Then
        subcategories.observeForTesting {
            val answer = subcategories.value!!
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
}
