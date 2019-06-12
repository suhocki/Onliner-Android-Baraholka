package kt.school.starlord.model.mapper

abstract class BaseConverter<FROM, TO>(
    override val fromClass: Class<FROM>,
    override val toClass: Class<TO>
) : Converter<FROM, TO>