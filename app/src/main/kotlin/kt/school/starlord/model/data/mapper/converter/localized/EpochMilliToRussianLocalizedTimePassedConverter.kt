package kt.school.starlord.model.data.mapper.converter.localized

import kt.school.starlord.domain.data.mapper.BaseConverter
import kt.school.starlord.domain.entity.global.EpochMilli
import kt.school.starlord.domain.entity.global.RussianLocalizedTimePassed


/**
 * Converts EpochMilli to Russian LocalizedTimePassed entity from UI layer.
 */
class EpochMilliToRussianLocalizedTimePassedConverter(
    private val epochMilliToLocalizedTimePassedConverter: EpochMilliToLocalizedTimePassedConverter
) : BaseConverter<EpochMilli, RussianLocalizedTimePassed>(
    EpochMilli::class.java,
    RussianLocalizedTimePassed::class.java
) {

    override fun convert(value: EpochMilli): RussianLocalizedTimePassed {
        val string = epochMilliToLocalizedTimePassedConverter.convert(value).value
        return RussianLocalizedTimePassed(string)
    }
}
