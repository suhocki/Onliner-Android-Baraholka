package kt.school.starlord.model.data.mapper.converter.localization

import kt.school.starlord.R
import kt.school.starlord.domain.data.mapper.BaseConverter
import kt.school.starlord.domain.entity.global.EpochMilli
import kt.school.starlord.domain.entity.global.LocalizedTimePassed
import kt.school.starlord.domain.entity.global.TimeUnit
import kt.school.starlord.model.data.resources.ResourceManager
import org.threeten.bp.Duration

/**
 * Converts EpochMilli to LocalizedTimePassed entity from UI layer.
 */
open class EpochMilliToLocalizedTimePassedConverter(
    private val resources: ResourceManager
) : BaseConverter<EpochMilli, LocalizedTimePassed>(EpochMilli::class.java, LocalizedTimePassed::class.java) {

    override fun convert(value: EpochMilli): LocalizedTimePassed {
        val epochMilli = value.value

        return LocalizedTimePassed(
            when {
                epochMilli >= TimeUnit.MONTH.millis -> {
                    val month = Duration.ofMillis(epochMilli).toDays() / 30
                    resources.getPlural(R.plurals.month_ago, month.toInt(), month)
                }
                epochMilli >= TimeUnit.DAY.millis -> {
                    val days = Duration.ofMillis(epochMilli).toDays()
                    resources.getPlural(R.plurals.days_ago, days.toInt(), days)
                }
                epochMilli >= TimeUnit.HOUR.millis -> {
                    val hours = Duration.ofMillis(epochMilli).toHours()
                    resources.getPlural(R.plurals.hours_ago, hours.toInt(), hours)
                }
                epochMilli >= TimeUnit.MINUTE.millis -> {
                    val minutes = Duration.ofMillis(epochMilli).toMinutes()
                    resources.getPlural(R.plurals.minutes_ago, minutes.toInt(), minutes)
                }
                else -> resources.getString(R.string.less_than_minute_ago)
            }
        )
    }
}
