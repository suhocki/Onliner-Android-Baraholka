package kt.school.starlord.model.data.mapper.converter

import kt.school.starlord.R
import kt.school.starlord.domain.data.mapper.BaseConverter
import kt.school.starlord.domain.entity.global.TimeUnit
import kt.school.starlord.model.data.resources.ResourceManager
import kt.school.starlord.ui.global.entity.wrapper.LocalizedTimePassed
import org.threeten.bp.Duration

/**
 * Converts millis (Long) to LocalizedTimePassed entity from UI layer.
 */
class LongToLocalizedTimePassedConverter(
    private val resources: ResourceManager
) : BaseConverter<Long, LocalizedTimePassed>(
    Long::class.java,
    LocalizedTimePassed::class.java
) {
    override fun convert(value: Long) = LocalizedTimePassed(
        when {
            value >= TimeUnit.MONTH.millis -> resources.getString(R.string.more_than_month_ago)
            value >= TimeUnit.DAY.millis -> {
                val days = Duration.ofMillis(value).toDays()
                resources.getPlural(R.plurals.days_ago, days.toInt(), days)
            }
            value >= TimeUnit.HOUR.millis -> {
                val hours = Duration.ofMillis(value).toHours()
                resources.getPlural(R.plurals.hours_ago, hours.toInt(), hours)
            }
            value >= TimeUnit.MINUTE.millis -> {
                val minutes = Duration.ofMillis(value).toMinutes()
                resources.getPlural(R.plurals.minutes_ago, minutes.toInt(), minutes)
            }
            else -> resources.getString(R.string.less_than_minute_ago)
        }
    )
}
