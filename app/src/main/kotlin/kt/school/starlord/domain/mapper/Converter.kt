package kt.school.starlord.domain.mapper

import kotlin.reflect.KClass

/**
 * Converter helps us to convert different objects to necessary types.
 */
interface Converter<FROM : Any, TO : Any> {
    val fromClass: KClass<FROM>
    val toClass: KClass<TO>

    /**
     * Converts input value to specified class.
     */
    fun convert(value: FROM): TO
}
