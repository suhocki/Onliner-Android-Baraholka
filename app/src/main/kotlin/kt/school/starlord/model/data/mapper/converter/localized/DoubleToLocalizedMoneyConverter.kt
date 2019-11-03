package kt.school.starlord.model.data.mapper.converter.localized

import kt.school.starlord.R
import kt.school.starlord.domain.data.mapper.BaseConverter
import kt.school.starlord.model.data.resources.ResourceManager
import kt.school.starlord.ui.global.entity.wrapper.LocalizedMoney
import kotlin.math.roundToLong

/**
 * Converts millis (Long) LocalizedTimePassed entity from UI layer.
 */
class DoubleToLocalizedMoneyConverter(
    private val resources: ResourceManager
) : BaseConverter<Double, LocalizedMoney>(Double::class.java, LocalizedMoney::class.java) {

    override fun convert(value: Double): LocalizedMoney {
        return LocalizedMoney(
            if (value == 0.0) resources.getString(R.string.for_free)
            else resources.getString(R.string.price, value.toRoundedPrice())
        )
    }

    private fun Double.toRoundedPrice() =
        ((this * MAX_PERCENTS).roundToLong() / MAX_PERCENTS_DOUBLE).toString()
            .replaceAfter(DOT_STRING, EMPTY_STRING)
            .replace(DOT_STRING, EMPTY_STRING)

    companion object {
        private const val EMPTY_STRING = ""
        private const val DOT_STRING = "."

        private const val MAX_PERCENTS = 100
        private const val MAX_PERCENTS_DOUBLE = 100.0
    }
}
