package kt.school.starlord.model.data.mapper.converter.localization

import kt.school.starlord.R
import kt.school.starlord.domain.data.mapper.BaseConverter
import kt.school.starlord.domain.entity.product.Price
import kt.school.starlord.model.data.resources.ResourceManager
import kt.school.starlord.ui.global.entity.wrapper.LocalizedMoney
import java.text.DecimalFormat

/**
 * Converts Price from domain to LocalizedMoney entity from UI layer.
 */
class PriceToLocalizedMoneyConverter(
    private val resources: ResourceManager
) : BaseConverter<Price, LocalizedMoney>(Price::class, LocalizedMoney::class) {

    override fun convert(value: Price): LocalizedMoney {
        return LocalizedMoney(
            if (value.amount == 0.0) resources.getString(R.string.for_free)
            else resources.getString(R.string.price, DecimalFormat(PRICE_FORMAT).format(value.amount))
        )
    }

    companion object {
        private const val PRICE_FORMAT = "#.##"
    }
}
