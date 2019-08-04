package kt.school.starlord.model.data.room.converter

import androidx.room.TypeConverter
import com.squareup.moshi.Moshi
import kt.school.starlord.domain.data.room.RoomConverter
import kt.school.starlord.entity.product.ProductOwner
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * Converts ProductOwner to String.
 */
class ProductOwnerConverter : RoomConverter<ProductOwner, String>, KoinComponent {

    private val moshi: Moshi by inject()
    private val adapter by lazy { moshi.adapter(ProductOwner::class.java) }

    @TypeConverter
    override fun toRoomType(value: ProductOwner): String = adapter.toJson(value)

    @TypeConverter
    override fun fromRoomType(value: String) = adapter.fromJson(value)
}
