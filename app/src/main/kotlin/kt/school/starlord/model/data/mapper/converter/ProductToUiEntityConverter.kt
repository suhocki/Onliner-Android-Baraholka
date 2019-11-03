package kt.school.starlord.model.data.mapper.converter

import android.view.View
import kt.school.starlord.R
import kt.school.starlord.domain.entity.product.Product
import kt.school.starlord.domain.entity.product.Price
import kt.school.starlord.domain.data.mapper.BaseConverter
import kt.school.starlord.ui.global.entity.UiEntity
import kt.school.starlord.ui.products.entity.UiProduct
import org.threeten.bp.Instant
import kotlin.math.roundToLong

/**
 * Converts Product entity from domain layer to Product entity from UI layer.
 */
class ProductToUiEntityConverter(
    private val longToLocalizedTimePassedConverter: LongToLocalizedTimePassedConverter,
    private val doubleToLocalizedMoneyConverter: DoubleToLocalizedMoneyConverter
) : BaseConverter<Product, UiEntity>(
    Product::class.java,
    UiEntity::class.java
) {
    override fun convert(value: Product) = UiProduct(
        id = value.id,
        title = value.title,
        description = value.description,
        type = value.type.name,
        typeColor = value.type.color,
        location = value.location,
        image = value.image,
        owner = value.owner.name,
        lastUpdate = longToLocalizedTimePassedConverter.convert(Instant.now().toEpochMilli() - value.timestamp.time).value,
        isPaid = value.isPaid,
        comments = value.commentsCount.toString(),
        price = value.price.amount?.let { doubleToLocalizedMoneyConverter.convert(it).value } ?: "",
        commentsCountVisibility = if (value.commentsCount > 0) View.VISIBLE else View.GONE,
        priceVisibility = if (value.price.amount != null) View.VISIBLE else View.GONE,
        bargainVisibility = if (value.price.isBargainAvailable) View.VISIBLE else View.GONE
    )
}
