package kt.school.starlord.model.data.mapper.converter

import java.net.URL
import kt.school.starlord.BuildConfig
import kt.school.starlord.model.data.mapper.entity.BaseConverter

/**
 * Contains logic on how to convert String to Url.
 */
class StringToUrlConverter : BaseConverter<String, URL>(
    String::class.java, URL::class.java
) {
    override fun convert(value: String): URL {
        val correctedPath = if (value.startsWith(DOT_SLASH)) {
            value.replace(DOT_SLASH, BuildConfig.BARAHOLKA_ONLINER_URL + SLASH)
        } else {
            value
        }
        return URL(correctedPath)
    }

    companion object {
        private const val DOT_SLASH = "./"
        private const val SLASH = "/"
    }
}
