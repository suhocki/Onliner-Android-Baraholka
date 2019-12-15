package kt.school.starlord.model.data.mapper.converter

import kt.school.starlord.domain.mapper.BaseConverter
import kt.school.starlord.domain.mapper.Mapper
import kt.school.starlord.domain.entity.product.Product
import kt.school.starlord.model.data.room.entity.RoomProduct
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * Converts RoomProduct entity from data layer to Product entity from domain layer.
 */
class RoomProductToProductConverter : BaseConverter<RoomProduct, Product>(
    RoomProduct::class,
    Product::class
), KoinComponent {

    private val mapper: Mapper by inject()

    override fun convert(value: RoomProduct) = Product(
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
        subcategoryId = value.subcategoryId,
        lastUpdate = value.lastUpdate,
        localizedTimePassed = mapper.map(value.lastUpdate)
    )
}
