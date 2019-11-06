package kt.school.starlord.model.data.mapper.converter.localization

import kt.school.starlord.domain.data.mapper.BaseConverter
import kt.school.starlord.ui.global.entity.wrapper.LocalizedTimePassed
import org.threeten.bp.Instant

/**
 * Time passed after some action, represented by LocalizedTimePassed, will be converted to long.
 * E.g. "5 seconds ago" will become (epochMilli() - 5 seconds)
 */
class LocalizedTimePassedToEpochMilliConverter : BaseConverter<LocalizedTimePassed, Long>(
    LocalizedTimePassed::class.java, Long::class.java
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
        SECONDS("меньше", 1000L),
        MINUTES("м", 60 * SECONDS.millis),
        HOURS("ч", 60 * MINUTES.millis),
        DAYS("д", 24 * HOURS.millis),
        MONTH("более", 30 * DAYS.millis)
    }

    companion object {
        private val REGEX_DATA = ("^(\\d+) ([${TimeType.MINUTES.identifier}" +
                "${TimeType.HOURS.identifier}${TimeType.DAYS.identifier}])").toRegex()

        private const val MIN_PARTS_COUNT = 3
        private const val INDEX_OF_NUMBER = 1
        private const val INDEX_OF_TIME_TYPE = 2
    }
}
