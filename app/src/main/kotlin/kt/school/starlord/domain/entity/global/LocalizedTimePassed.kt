package kt.school.starlord.domain.entity.global

/**
 * Wrapper for human-readable time passed after some action.
 * E.g. "5 seconds ago".
 */
class LocalizedTimePassed(val value: String) {

    override fun equals(other: Any?) = other is LocalizedTimePassed &&
            (value == other.value ||
                    (other.value.startsWith("более 1 месяца") && value.contains("месяц")))

    override fun hashCode(): Int {
        return value.hashCode()
    }
}