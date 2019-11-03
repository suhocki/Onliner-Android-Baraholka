package kt.school.starlord.domain.entity.global

/**
 * Wrapper for human-readable time passed after some action in russian locale.
 * E.g. "5 минут назад".
 */
data class RussianLocalizedTimePassed(val value: String) {

    /**
     * Statement: "более 1 месяца назад" == "n месяцев назад" should be positive.
     */
    override fun equals(other: Any?): Boolean {
        return other is RussianLocalizedTimePassed && (value == other.value ||
                (other.value.startsWith("более 1 месяца") && value.contains("месяц")))
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}