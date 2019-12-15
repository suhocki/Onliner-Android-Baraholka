package kt.school.starlord.model.data.mapper.converter.localization

import kt.school.starlord.domain.mapper.BaseConverter
import kt.school.starlord.ui.global.entity.wrapper.LocalizedTimePassed
import org.threeten.bp.Instant

/**
 * Time passed after some action, represented by LocalizedTimePassed, will be converted to long.
 * E.g. "5 seconds ago" will become (lastUpdate() - 5 seconds)
 */
class LocalizedTimePassedToLongConverter : BaseConverter<LocalizedTimePassed, Long>(
    LocalizedTimePassed::class, Long::class
) {

    override fun convert(value: LocalizedTimePassed): Long {
        val string = value.value
        val now = Instant.now()

        return when {
            string.startsWith(TimeType.SECONDS.identifier) -> now.toEpochMilli()
            string.startsWith(TimeType.MONTH.identifier) -> now.minusMillis(TimeType.MONTH.millis).toEpochMilli()
            else -> {
                val parsed = REGEX_DATA.find(string)?.groupValues
                    ?: error("Cannot parse EpochMilli from $value")

                if (parsed.size < MIN_PARTS_COUNT) error("Cannot parse EpochMilli from $value")

                val number = parsed[INDEX_OF_NUMBER].toLong()
                val timeType =
                    TimeType.values().first { it.identifier == parsed[INDEX_OF_TIME_TYPE] }

                now.toEpochMilli() - number * timeType.millis
            }
        }
    }

    private enum class TimeType(val identifier: String, val millis: Long) {
        SECONDS("меньше", MILLIS_IN_SECOND),
        MINUTES("м", SECONDS_IN_MINUTE * SECONDS.millis),
        HOURS("ч", MINUTES_IN_HOUR * MINUTES.millis),
        DAYS("д", HOURS_IN_DAY * HOURS.millis),
        MONTH("более", DAYS_IN_MONTH * DAYS.millis)
    }

    companion object {
        private val REGEX_DATA = ("^(\\d+) ([${TimeType.MINUTES.identifier}" +
                "${TimeType.HOURS.identifier}${TimeType.DAYS.identifier}])").toRegex()

        private const val MIN_PARTS_COUNT = 3
        private const val INDEX_OF_NUMBER = 1
        private const val INDEX_OF_TIME_TYPE = 2

        private const val MILLIS_IN_SECOND = 1000L
        private const val SECONDS_IN_MINUTE = 60
        private const val MINUTES_IN_HOUR = 60
        private const val HOURS_IN_DAY = 24
        private const val DAYS_IN_MONTH = 30
    }
}
