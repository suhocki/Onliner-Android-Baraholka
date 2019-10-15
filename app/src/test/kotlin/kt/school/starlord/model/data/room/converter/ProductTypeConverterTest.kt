package kt.school.starlord.model.data.room.converter

import kt.school.starlord.domain.entity.product.ProductType
import org.junit.Test

class ProductTypeConverterTest {
    private val converter = ProductTypeConverter()

    @Test
    fun `to Room type`() {
        // Given
        val productType = ProductType.RENT

        // When
        val answer = converter.toRoomType(productType)

        // Then
        assert(ProductType.valueOf(answer) == productType)
    }

    @Test
    fun `from Room type`() {
        // Given
        val string = ProductType.RENT.name

        // When
        val answer = converter.fromRoomType(string)

        // Then
        assert(answer == ProductType.RENT)
    }
}
