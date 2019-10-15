package kt.school.starlord.model.data.room.converter

import androidx.room.TypeConverter
import kt.school.starlord.domain.data.room.RoomConverter
import kt.school.starlord.domain.entity.product.ProductType

/**
 * Converts ProductType to String.
 */
class ProductTypeConverter : RoomConverter<ProductType, String> {

    @TypeConverter
    override fun toRoomType(value: ProductType) = value.name

    @TypeConverter
    override fun fromRoomType(value: String) = ProductType.valueOf(value)
}
