package kt.school.starlord.extension

import kotlin.math.roundToLong

private const val MAX_PERCENTS = 100
private const val MAX_PERCENTS_DOUBLE = 100.0
/**
 * Rounds to 2 points after comma.
 */
fun Double.toRoundedPrice() =
    ((this * MAX_PERCENTS).roundToLong() / MAX_PERCENTS_DOUBLE).toString()
