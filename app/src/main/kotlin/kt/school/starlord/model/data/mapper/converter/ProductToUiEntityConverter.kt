package kt.school.starlord.model.data.mapper.converter

import android.view.View
import kt.school.starlord.domain.entity.product.Product
import kt.school.starlord.domain.data.mapper.BaseConverter
import kt.school.starlord.domain.data.mapper.Mapper
import kt.school.starlord.domain.entity.global.EpochMilli
import kt.school.starlord.domain.entity.global.LocalizedTimePassed
import kt.school.starlord.ui.global.entity.UiEntity
import kt.school.starlord.ui.global.entity.wrapper.LocalizedMoney
import kt.school.starlord.ui.products.entity.UiProduct
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.threeten.bp.Instant

/**
 * Converts Product entity from domain layer to Product entity from UI layer.
 */
class ProductToUiEntityConverter : BaseConverter<Product, UiEntity>(
    Product::class.java,
    UiEntity::class.java
), KoinComponent {

    private val mapper: Mapper by inject()

    override fun convert(value: Product): UiProduct {
        val epochMilliNow = Instant.now().toEpochMilli()
        val productEpochMilli = EpochMilli(epochMilliNow - value.epochMilli.value)

        return UiProduct(
            id = value.id,
            title = value.title,
            description = value.description,
            type = value.type.name,
            typeColor = value.type.color,
            location = value.location,
            image = value.image,
            owner = value.owner.name,
            lastUpdate = mapper.map<LocalizedTimePassed>(productEpochMilli).value,
            isPaid = value.isPaid,
            comments = value.commentsCount.toString(),
            price = mapper.map<LocalizedMoney>(value.price).value,
            commentsCountVisibility = if (value.commentsCount > 0) View.VISIBLE else View.GONE,
            priceVisibility = if (value.price.hasPrice) View.VISIBLE else View.GONE,
            bargainVisibility = if (value.price.isBargainAvailable) View.VISIBLE else View.GONE
        )
    }
}
