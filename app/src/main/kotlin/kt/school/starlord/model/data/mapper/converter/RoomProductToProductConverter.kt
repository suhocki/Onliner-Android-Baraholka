package kt.school.starlord.model.data.mapper.converter

import kt.school.starlord.domain.data.mapper.BaseConverter
import kt.school.starlord.domain.data.mapper.Mapper
import kt.school.starlord.domain.entity.global.EpochMilli
import kt.school.starlord.domain.entity.product.LastUpdate
import kt.school.starlord.domain.entity.product.Product
import kt.school.starlord.model.data.room.entity.RoomProduct
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * Converts RoomProduct entity from data layer to Product entity from domain layer.
 */
class RoomProductToProductConverter : BaseConverter<RoomProduct, Product>(
    RoomProduct::class.java,
    Product::class.java
), KoinComponent {

    private val mapper: Mapper by inject()

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
            lastUpdate = LastUpdate(epochMilli, mapper.map(epochMilli))
        )
    }
}
