package kt.school.starlord.domain.data.mapper

/**
 * Designed for mapping objects to different types using converters.
 */
class Mapper(inline val converters: Set<Converter<*, *>>) {

    /**
     * Maps input object to specified type.
     *
     * @return required type object
     * @throws NoSuchElementException when there is no appropriate converter
     */
    inline fun <reified To : Any> map(input: Any): To {
        if (input is To) return input

        val converter = converters.find { it.fromClass == input::class && To::class == it.toClass }
                ?: throw NoSuchElementException("Cannot find converter from ${input::class} to ${To::class}")

        @Suppress("UNCHECKED_CAST")
        return (converter as Converter<Any, To>).convert(input)
    }
}
