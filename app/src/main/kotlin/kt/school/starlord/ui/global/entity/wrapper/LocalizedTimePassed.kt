package kt.school.starlord.ui.global.entity.wrapper

/**
 * Wrapper for human-readable time passed after some action.
 * E.g. "5 seconds ago".
 */
class LocalizedTimePassed(val value: String) {

    override fun equals(other: Any?) = other is LocalizedTimePassed &&
            (value == other.value || (other.value.startsWith(ServerConstant.MORE_THAN_A_MONTH_AGO) &&
                    value.contains(ServerConstant.MONTH)))

    override fun hashCode(): Int {
        return value.hashCode()
    }
}

private object ServerConstant {
    const val MORE_THAN_A_MONTH_AGO = "более 1 месяца"
    const val MONTH = "месяц"
}