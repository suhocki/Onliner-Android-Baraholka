package kt.school.starlord.model.data.mapper.converter.localization

import kt.school.starlord.domain.data.mapper.BaseConverter
import kt.school.starlord.domain.entity.global.EpochMilli
import kt.school.starlord.domain.entity.global.LocalizedTimePassed
import kt.school.starlord.domain.entity.global.TimeUnit
import org.threeten.bp.Instant

/**
 * Time passed after some action, represented by LocalizedTimePassed, will be converted to EpochMilli.
 * E.g. "5 seconds ago" will become (epochMilli() - 5 seconds)
 */
class LocalizedTimePassedToEpochMilliConverter : BaseConverter<LocalizedTimePassed, EpochMilli>(
    LocalizedTimePassed::class.java, EpochMilli::class.java
) {

    override fun convert(value: LocalizedTimePassed): EpochMilli {
        val string = value.value
        val now = Instant.now()

        return EpochMilli(when {
            string.startsWith(TimeType.SECONDS.identifier) -> now.toEpochMilli()
            string.startsWith(TimeType.MAX.identifier) -> now.minusMillis(TimeUnit.MONTH.millis).toEpochMilli()
            else -> {
                val parsed = REGEX_DATA.find(string)?.groupValues
                    ?: error("Cannot parse EpochMilli from $value")

                if (parsed.size < MIN_PARTS_COUNT) error("Cannot parse EpochMilli from $value")

                val number = parsed[INDEX_OF_NUMBER].toLong()
                val timeType = TimeType.values().first { it.identifier == parsed[INDEX_OF_TIME_TYPE] }

                now.toEpochMilli() - number * timeType.timeUnit.millis
            }
        })
    }

    private enum class TimeType(val identifier: String, val timeUnit: TimeUnit) {
        SECONDS("меньше", TimeUnit.SECOND),
        MINUTES("м", TimeUnit.MINUTE),
        HOURS("ч", TimeUnit.HOUR),
        DAYS("д", TimeUnit.DAY),
        MAX("более", TimeUnit.MONTH)
    }

    companion object {
        private val REGEX_DATA = ("^(\\d+) ([${TimeType.MINUTES.identifier}" +
                "${TimeType.HOURS.identifier}${TimeType.DAYS.identifier}])").toRegex()

        private const val MIN_PARTS_COUNT = 3
        private const val INDEX_OF_NUMBER = 1
        private const val INDEX_OF_TIME_TYPE = 2
    }
}
