package kt.school.starlord.model.data.room.converter

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import io.mockk.every
import io.mockk.mockk
import kt.school.starlord.domain.entity.product.Price
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.AutoCloseKoinTest
import org.koin.test.mock.declare
import org.mockito.ArgumentMatchers

@RunWith(AndroidJUnit4::class)
class PriceConverterTest : AutoCloseKoinTest() {

    private val moshi: Moshi = mockk()
    private val adapter = mockk<JsonAdapter<Price>>()
    private val converter by lazy { PriceConverter() }

    @Before
    fun setUp() {
        declare {
            single { moshi }
        }

        every { moshi.adapter(Price::class.java) } returns adapter
    }

    @Test
    fun `from Room type`() {
        // Given
        val json = ArgumentMatchers.anyString()
        val price: Price = mockk()

        every { adapter.fromJson(json) } returns price

        // When
        val result = converter.fromRoomType(json)

        // Then
        assert(result == price)
    }

    @Test
    fun `to Room type`() {
        // Given
        val json = ArgumentMatchers.anyString()
        val price: Price = mockk()

        every { adapter.toJson(price) } returns json

        // When
        val result = converter.toRoomType(price)

        // Then
        assert(result == json)
    }
}
