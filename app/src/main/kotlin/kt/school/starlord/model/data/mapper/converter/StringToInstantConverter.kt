package kt.school.starlord.model.data.mapper.converter

import kt.school.starlord.model.data.mapper.entity.BaseConverter
import org.threeten.bp.Instant

/**
 * Convert String to Instant.
 * "5 seconds ago" -> System.currentTimeMillis() - 5 seconds
 */
class StringToInstantConverter : BaseConverter<String, Instant>(
    String::class.java, Instant::class.java
) {
    override fun convert(value: String): Instant = when {
        value.startsWith(TimeType.SECONDS.identifier) -> Instant.now()
        value.startsWith(TimeType.MAX.identifier) -> Instant.ofEpochMilli(0)
        else -> Instant.now().minusMillis(getDelta(value))
    }

    private fun getDelta(value: String): Long {
        val parsed = REGEX_DATA.find(value)?.groupValues ?: error("Cannot parse Date from $value")

        if (parsed.size < MIN_PARTS_COUNT) error("Cannot parse Date from $value")

        val number = parsed[INDEX_OF_NUMBER].toLong()
        val timeType = TimeType.values().first { it.identifier == parsed[INDEX_OF_TIME_TYPE] }
        return number * timeType.timeFactor
    }

    companion object {
        private val REGEX_DATA = ("^(\\d+) ([${TimeType.MINUTES.identifier}" +
                "${TimeType.HOURS.identifier}${TimeType.DAYS.identifier}])").toRegex()

        private const val MIN_PARTS_COUNT = 3
        private const val INDEX_OF_NUMBER = 1
        private const val INDEX_OF_TIME_TYPE = 2
        private const val MILLIS_IN_SEC = 1000L
        private const val SECONDS_IN_MINUTE = 60
        private const val MINUTES_IN_HOUR = 60
        private const val HOURS_IN_DAY = 24
    }

    private enum class TimeType(val identifier: String, val timeFactor: Long) {
        SECONDS("меньше", MILLIS_IN_SEC),
        MINUTES("м", SECONDS_IN_MINUTE * SECONDS.timeFactor),
        HOURS("ч", MINUTES_IN_HOUR * MINUTES.timeFactor),
        DAYS("д", HOURS_IN_DAY * HOURS.timeFactor),
        MAX("более", -1)
    }
}
