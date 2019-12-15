package kt.school.starlord.model.data.mapper.converter.localization

import kt.school.starlord.R
import kt.school.starlord.domain.mapper.BaseConverter
import kt.school.starlord.model.data.android.ResourceManager
import kt.school.starlord.ui.global.entity.wrapper.LocalizedTimePassed
import org.threeten.bp.Duration

/**
 * Converts Long to LocalizedTimePassed entity from UI layer.
 */
open class LongToLocalizedTimePassedConverter(
    private val resources: ResourceManager
) : BaseConverter<Long, LocalizedTimePassed>(Long::class, LocalizedTimePassed::class) {

    override fun convert(value: Long) = LocalizedTimePassed(
        when {
            value >= MILLIS_IN_MONTH -> {
                val month = Duration.ofMillis(value).toDays() / DAYS_IN_MONTH
                resources.getPlural(R.plurals.month_ago, month.toInt(), month)
            }
            value >= MILLIS_IN_DAY -> {
                val days = Duration.ofMillis(value).toDays()
                resources.getPlural(R.plurals.days_ago, days.toInt(), days)
            }
            value >= MILLIS_IN_HOUR -> {
                val hours = Duration.ofMillis(value).toHours()
                resources.getPlural(R.plurals.hours_ago, hours.toInt(), hours)
            }
            value >= MILLIS_IN_MINUTE -> {
                val minutes = Duration.ofMillis(value).toMinutes()
                resources.getPlural(R.plurals.minutes_ago, minutes.toInt(), minutes)
            }
            else -> resources.getString(R.string.less_than_minute_ago)
        }
    )

    companion object {
        private const val DAYS_IN_MONTH = 30
        private const val MILLIS_IN_MINUTE = 60 * 1000L
        private const val MILLIS_IN_HOUR = 60 * MILLIS_IN_MINUTE
        private const val MILLIS_IN_DAY = 24 * MILLIS_IN_HOUR
        private const val MILLIS_IN_MONTH = 30 * MILLIS_IN_DAY
    }
}
