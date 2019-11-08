package kt.school.starlord.model.data.mapper.converter.localization

import kt.school.starlord.domain.data.mapper.BaseConverter
import kt.school.starlord.domain.data.mapper.Mapper
import kt.school.starlord.ui.global.entity.wrapper.LocalizedTimePassed
import kt.school.starlord.domain.entity.global.RussianLocalizedTimePassed
import org.koin.core.KoinComponent
import org.koin.core.inject


/**
 * Converts Long to Russian LocalizedTimePassed entity.
 */
class LongToRussianLocalizedTimePassedConverter : BaseConverter<Long, RussianLocalizedTimePassed>(
    Long::class,
    RussianLocalizedTimePassed::class
), KoinComponent {

    private val mapper: Mapper by inject()

    override fun convert(value: Long): RussianLocalizedTimePassed {
        return RussianLocalizedTimePassed(mapper.map<LocalizedTimePassed>(value).value)
    }
}
