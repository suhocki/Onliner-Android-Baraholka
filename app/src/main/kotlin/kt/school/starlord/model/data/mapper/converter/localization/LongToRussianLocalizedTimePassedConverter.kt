package kt.school.starlord.model.data.mapper.converter.localization

import kt.school.starlord.domain.data.mapper.BaseConverter
import kt.school.starlord.domain.data.mapper.Mapper
import kt.school.starlord.ui.global.entity.wrapper.LocalizedTimePassed
import kt.school.starlord.domain.entity.global.RussianLocalizedTimePassed
import kt.school.starlord.model.data.resources.ResourceManager
import org.koin.core.KoinComponent
import org.koin.core.inject


/**
 * Converts Long to Russian LocalizedTimePassed entity.
 */
class LongToRussianLocalizedTimePassedConverter(
    resourceManager: ResourceManager
) : BaseConverter<Long, RussianLocalizedTimePassed>(
    Long::class,
    RussianLocalizedTimePassed::class
), KoinComponent {

    private val converter = LongToLocalizedTimePassedConverter(resourceManager)

    override fun convert(value: Long): RussianLocalizedTimePassed {
        return RussianLocalizedTimePassed(converter.convert(value).value)
    }
}
