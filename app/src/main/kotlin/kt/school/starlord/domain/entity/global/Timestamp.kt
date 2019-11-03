package kt.school.starlord.domain.entity.global

/**
 * Contains information about the time when some action occurred.
 *
 * @param time exact time when the action was performed.
 */
data class Timestamp(
    val time: Long,
    val interval: TimeUnit
)
