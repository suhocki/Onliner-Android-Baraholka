package kt.school.starlord.model.data.mapper.converter

import kt.school.starlord.domain.data.mapper.BaseConverter
import kt.school.starlord.domain.entity.global.EpochMilli
import kt.school.starlord.domain.entity.product.LastUpdate
import kt.school.starlord.domain.entity.product.Product
import kt.school.starlord.model.data.mapper.converter.localized.EpochMilliToLocalizedTimePassedConverter
import kt.school.starlord.model.data.room.entity.RoomProduct

/**
 * Converts RoomProduct entity from data layer to Product entity from domain layer.
 */
class RoomProductToProductConverter(
    private val epochMilliToLocalizedTimePassedConverter: EpochMilliToLocalizedTimePassedConverter
) : BaseConverter<RoomProduct, Product>(RoomProduct::class.java, Product::class.java) {

    override fun convert(value: RoomProduct): Product {
        val epochMilli = EpochMilli(value.lastUpdate)
        return Product(
            id = value.id,
            title = value.title,
            description = value.description,
            type = value.type,
            location = value.location,
            image = value.image,
            owner = value.owner,
            price = value.price,
            commentsCount = value.commentsCount,
            isPaid = value.isPaid,
            subcategoryName = value.subcategoryName,
            lastUpdate = LastUpdate(epochMilli, epochMilliToLocalizedTimePassedConverter.convert(epochMilli))
        )
    }
}
