package kt.school.starlord.model.data.room.converter

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import io.mockk.every
import io.mockk.mockk
import kt.school.starlord.domain.entity.product.ProductOwner
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.AutoCloseKoinTest
import org.koin.test.mock.declare
import org.mockito.ArgumentMatchers.anyString

@RunWith(AndroidJUnit4::class)
class ProductOwnerConverterTest : AutoCloseKoinTest() {

    private val moshi: Moshi = mockk()
    private val adapter = mockk<JsonAdapter<ProductOwner>>()
    private val converter by lazy { ProductOwnerConverter() }

    @Before
    fun setUp() {
        declare {
            single { moshi }
        }

        every { moshi.adapter(ProductOwner::class.java) } returns adapter
    }

    @Test
    fun `from Room type`() {
        // Given
        val json = anyString()
        val productOwner: ProductOwner = mockk()

        every { adapter.fromJson(json) } returns productOwner

        // When
        val result = converter.fromRoomType(json)

        // Then
        assert(result == productOwner)
    }

    @Test
    fun `to Room type`() {
        // Given
        val json = anyString()
        val productOwner: ProductOwner = mockk()

        every { adapter.toJson(productOwner) } returns json

        // When
        val result = converter.toRoomType(productOwner)

        // Then
        assert(result == json)
    }
}
