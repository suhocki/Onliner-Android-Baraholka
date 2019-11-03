package kt.school.starlord.domain.entity.product

import kt.school.starlord.domain.entity.global.EpochMilli
import kt.school.starlord.domain.entity.global.LocalizedTimePassed

/**
 * @param epochMilli how many milliseconds have elapsed before some action.
 * @param localizedTimePassed localized human-readable time elapsed after some action to the present.
 */
data class LastUpdate(
    var epochMilli: EpochMilli,
    val localizedTimePassed: LocalizedTimePassed
)