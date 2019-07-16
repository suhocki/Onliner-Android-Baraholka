package kt.school.starlord.extension

import kotlin.math.roundToLong

/**
 * Rounds to 2 points after comma.
 */
fun Double.toRoundedPrice() =
    ((this * 100).roundToLong() / 100.0).toString()
