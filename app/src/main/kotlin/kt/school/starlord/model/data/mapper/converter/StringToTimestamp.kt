package kt.school.starlord.model.data.mapper.converter

import kt.school.starlord.domain.entity.global.TimeUnit
import kt.school.starlord.domain.entity.global.Timestamp
import kt.school.starlord.domain.data.mapper.BaseConverter
import org.threeten.bp.Instant

/**
 * Converts String to Timestamp.
 */
class StringToTimestamp : BaseConverter<String, Timestamp>(String::class.java, Timestamp::class.java) {

    override fun convert(value: String): Timestamp {
        val currentMillis = Instant.now().toEpochMilli()

        return when {
            value.startsWith(TimeType.SECONDS.identifier) -> Timestamp(
                time = currentMillis,
                interval = TimeUnit.MINUTE
            )
            value.startsWith(TimeType.MAX.identifier) -> Timestamp(
                time = 0L,
                interval = TimeUnit.MONTH
            )
            else -> {
                val parsed = REGEX_DATA.find(value)?.groupValues ?: error("Cannot parse Date from $value")

                if (parsed.size < MIN_PARTS_COUNT) error("Cannot parse Date from $value")

                val number = parsed[INDEX_OF_NUMBER].toLong()
                val timeType = TimeType.values().first { it.identifier == parsed[INDEX_OF_TIME_TYPE] }
                return Timestamp(
                    time = currentMillis - number * timeType.timeUnit.millis,
                    interval = timeType.timeUnit
                )
            }
        }
    }

    companion object {
        private val REGEX_DATA = ("^(\\d+) ([${TimeType.MINUTES.identifier}" +
                "${TimeType.HOURS.identifier}${TimeType.DAYS.identifier}])").toRegex()

        private const val MIN_PARTS_COUNT = 3
        private const val INDEX_OF_NUMBER = 1
        private const val INDEX_OF_TIME_TYPE = 2
    }

    private enum class TimeType(val identifier: String, val timeUnit: TimeUnit) {
        SECONDS("меньше", TimeUnit.SECOND),
        MINUTES("м", TimeUnit.MINUTE),
        HOURS("ч", TimeUnit.HOUR),
        DAYS("д", TimeUnit.DAY),
        MAX("более", TimeUnit.MONTH)
    }
}
