package kt.school.starlord.ui.global.entity.wrapper

/**
 * Wrapper for human-readable time passed after some action.
 * E.g. "5 seconds ago".
 */
data class LocalizedTimePassed(val value: String) {

    override fun equals(other: Any?): Boolean {
        return other is LocalizedTimePassed && (value == other.value ||
                other.value.startsWith(MORE_THAN_A_MONTH_AGO) && value.contains(MONTH))
    }

    override fun hashCode() = value.hashCode()

    companion object {
        const val MORE_THAN_A_MONTH_AGO = "более 1 месяца"
        const val MONTH = "месяц"
    }
}
