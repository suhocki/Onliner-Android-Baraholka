package kt.school.starlord.model.data.room.converter

import androidx.room.TypeConverter
import com.squareup.moshi.Moshi
import kt.school.starlord.domain.data.room.RoomConverter
import kt.school.starlord.domain.entity.product.Price
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * Converts Price to String.
 */
class ProductPriceConverter : RoomConverter<Price, String>, KoinComponent {

    private val moshi: Moshi by inject()
    private val adapter by lazy { moshi.adapter(Price::class.java) }

    @TypeConverter
    override fun toRoomType(value: Price): String = adapter.toJson(value)

    @TypeConverter
    override fun fromRoomType(value: String) = adapter.fromJson(value)
}
