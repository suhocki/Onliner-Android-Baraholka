package kt.school.starlord.model.mapper


class Mapper constructor(
    inline val converters: Set<Converter<*, *>>
) {
    @Suppress("UNCHECKED_CAST")
    inline fun <reified To> map(input: Any): To {
        if (input is To) return input

        val converter = findConverter<To>(input)
            ?: throw NoSuchElementException("Cannot find converter from ${input::class.java} to ${To::class.java}")

        return (converter as Converter<Any, To>).convert(input)
    }

    inline fun <reified To> findConverter(
        input: Any
    ) = converters.find {
        val isSuitableConverter =
            it.fromClass.isAssignableFrom(input::class.java) &&
                    To::class.java.isAssignableFrom(it.toClass)

        isSuitableConverter
    }
}