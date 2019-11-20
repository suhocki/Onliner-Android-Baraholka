package kt.school.starlord.domain.data.mapper

import kotlin.reflect.KClass

/**
 * Contains constructor with parameters for simplicity.
 */
abstract class BaseConverter<FROM : Any, TO : Any>(
    override val fromClass: KClass<FROM>,
    override val toClass: KClass<TO>
) : Converter<FROM, TO>
