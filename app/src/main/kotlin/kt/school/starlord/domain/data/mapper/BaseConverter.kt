package kt.school.starlord.domain.data.mapper

/**
 * Contains constructor with parameters for simplicity.
 */
abstract class BaseConverter<FROM, TO>(
    override val fromClass: Class<FROM>,
    override val toClass: Class<TO>
) : Converter<FROM, TO>
