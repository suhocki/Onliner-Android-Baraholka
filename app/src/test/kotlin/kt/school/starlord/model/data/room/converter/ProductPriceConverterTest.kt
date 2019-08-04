package kt.school.starlord.model.data.room.converter

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import io.mockk.every
import io.mockk.mockk
import kt.school.starlord.entity.product.ProductPrice
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.AutoCloseKoinTest
import org.koin.test.mock.declare
import org.mockito.ArgumentMatchers

@RunWith(AndroidJUnit4::class)
class ProductPriceConverterTest : AutoCloseKoinTest() {

    private val moshi: Moshi = mockk()
    private val adapter = mockk<JsonAdapter<ProductPrice>>()
    private val converter by lazy { ProductPriceConverter() }

    @Before
    fun setUp() {
        declare {
            single { moshi }
        }

        every { moshi.adapter(ProductPrice::class.java) } returns adapter
    }

    @Test
    fun `from Room type`() {
        // Given
        val json = ArgumentMatchers.anyString()
        val productPrice: ProductPrice = mockk()

        every { adapter.fromJson(json) } returns productPrice

        // When
        val result = converter.fromRoomType(json)

        // Then
        assert(result == productPrice)
    }

    @Test
    fun `to Room type`() {
        // Given
        val json = ArgumentMatchers.anyString()
        val productPrice: ProductPrice = mockk()

        every { adapter.toJson(productPrice) } returns json

        // When
        val result = converter.toRoomType(productPrice)

        // Then
        assert(result == json)
    }
}
