package kt.school.starlord.model.mapper

/**
 * Base abstract converter for TypeMapper. Was made to keep fromClass and toClass fields inside constructor.
 */
abstract class BaseConverter<FROM, TO>(
    override val fromClass: Class<FROM>,
    override val toClass: Class<TO>
) : Converter<FROM, TO>
