package kt.school.starlord.model.mapper

/**
 * Converter helps us to convert different objects to necessary types
 */
interface Converter<FROM, TO> {
    val fromClass: Class<FROM>
    val toClass: Class<TO>

    /**
     * Converts input value to specified class
     */
    fun convert(value: FROM): TO
}