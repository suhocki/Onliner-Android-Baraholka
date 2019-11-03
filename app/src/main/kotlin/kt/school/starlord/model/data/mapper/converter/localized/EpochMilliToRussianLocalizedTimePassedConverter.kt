package kt.school.starlord.model.data.mapper.converter.localized

import kt.school.starlord.domain.data.mapper.BaseConverter
import kt.school.starlord.domain.data.mapper.Mapper
import kt.school.starlord.domain.entity.global.EpochMilli
import kt.school.starlord.domain.entity.global.LocalizedTimePassed
import kt.school.starlord.domain.entity.global.RussianLocalizedTimePassed
import org.koin.core.KoinComponent
import org.koin.core.inject


/**
 * Converts EpochMilli to Russian LocalizedTimePassed entity from UI layer.
 */
class EpochMilliToRussianLocalizedTimePassedConverter : BaseConverter<EpochMilli, RussianLocalizedTimePassed>(
    EpochMilli::class.java,
    RussianLocalizedTimePassed::class.java
), KoinComponent {

    private val mapper: Mapper by inject()

    override fun convert(value: EpochMilli): RussianLocalizedTimePassed {
        val string = mapper.map<LocalizedTimePassed>(value).value
        return RussianLocalizedTimePassed(string)
    }
}
