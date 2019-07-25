package kt.school.starlord.model.data.mapper.entity

import kt.school.starlord.domain.data.mapper.Converter

/**
 * Contains constructor with parameters for simplicity.
 */
abstract class BaseConverter<FROM, TO>(
    override val fromClass: Class<FROM>,
    override val toClass: Class<TO>
) : Converter<FROM, TO>
