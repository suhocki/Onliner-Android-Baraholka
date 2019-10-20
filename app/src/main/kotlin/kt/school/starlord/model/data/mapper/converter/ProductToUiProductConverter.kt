package kt.school.starlord.model.data.mapper.converter

import android.view.View
import kt.school.starlord.R
import kt.school.starlord.domain.entity.product.Product
import kt.school.starlord.domain.entity.product.ProductPrice
import kt.school.starlord.model.data.mapper.entity.BaseConverter
import kt.school.starlord.model.data.resources.ResourceManager
import kt.school.starlord.ui.products.entity.UiProduct
import org.threeten.bp.Duration
import org.threeten.bp.Instant
import kotlin.math.roundToLong

/**
 * Converts domain object to ui.
 */
class ProductToUiProductConverter(private val resources: ResourceManager) :
    BaseConverter<Product, UiProduct>(
        Product::class.java, UiProduct::class.java
    ) {
    override fun convert(value: Product): UiProduct {
        val upTime = Instant.now().toEpochMilli() - value.lastUpdate
        val ofMillis = Duration.ofMillis(upTime)
        val lastUpdate = convertUpTime(upTime, ofMillis)
        val price = convertPrice(value.price)

        return UiProduct(
            id = value.id,
            title = value.title,
            description = value.description,
            type = value.type.name,
            typeColor = value.type.color,
            location = value.location,
            image = value.image,
            owner = value.owner.name,
            lastUpdate = lastUpdate,
            isPaid = value.isPaid,
            comments = value.commentsCount.toString(),
            price = price,
            commentsCountVisibility = if (value.commentsCount > 0) View.VISIBLE else View.GONE,
            priceVisibility = if (value.price.amount != null) View.VISIBLE else View.GONE,
            bargainVisibility = if (value.price.isBargainAvailable) View.VISIBLE else View.GONE
        )
    }

    private fun convertPrice(
        price: ProductPrice
    ): String {
        return when (price.amount) {
            null -> EMPTY_STRING // doesn`t matter, price will be hidden
            0.0 -> resources.getString(R.string.for_free)
            else -> resources.getString(R.string.price, price.amount.toRoundedPrice())
        }
    }

    private fun convertUpTime(upTime: Long, ofMillis: Duration) = when {
        upTime >= MILLIS_IN_MONTH -> resources.getString(R.string.more_than_month_ago)
        upTime >= MILLIS_IN_DAY -> {
            val days = ofMillis.toDays()
            resources.getPlural(R.plurals.days_ago, days.toInt(), days)
        }
        upTime >= MILLIS_IN_HOUR -> {
            val hours = ofMillis.toHours()
            resources.getPlural(R.plurals.hours_ago, hours.toInt(), hours)
        }
        upTime >= MILLIS_IN_MINUTE -> {
            val minutes = ofMillis.toMinutes()
            resources.getPlural(R.plurals.minutes_ago, minutes.toInt(), minutes)
        }
        else -> resources.getString(R.string.less_than_minute_ago)
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

        private const val MILLIS_IN_SEC = 1000L
        private const val MILLIS_IN_MINUTE = 60 * MILLIS_IN_SEC
        private const val MILLIS_IN_HOUR = 60 * MILLIS_IN_MINUTE
        private const val MILLIS_IN_DAY = 24 * MILLIS_IN_HOUR
        private const val MILLIS_IN_MONTH = 30 * MILLIS_IN_DAY
    }
}
