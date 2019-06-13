package kt.school.starlord.model.mapper

interface Converter<FROM, TO> {
    val fromClass: Class<FROM>
    val toClass: Class<TO>

    fun convert(value: FROM): TO
}