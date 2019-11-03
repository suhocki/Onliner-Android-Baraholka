package kt.school.starlord.domain.entity.global

/**
 * Contains info about how many milliseconds in some time interval.
 */
enum class TimeUnit(val millis: Long) {
    SECOND(1000L),
    MINUTE(60 * SECOND.millis),
    HOUR(60 * MINUTE.millis),
    DAY(24 * HOUR.millis),
    MONTH(30 * DAY.millis);

    companion object {
        /**
         * Converts millis to TimeUnit.
         */
        fun fromMillis(millis: Long) = values().find { it.millis == millis }
            ?: error("Cannot find appropriate TimeUnit for millis: $millis")
    }
}