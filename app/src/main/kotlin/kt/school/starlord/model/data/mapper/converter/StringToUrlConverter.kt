package kt.school.starlord.model.data.mapper.converter

import kt.school.starlord.BuildConfig
import kt.school.starlord.domain.data.mapper.BaseConverter
import java.net.URL

/**
 * Converts String to URL.
 */
class StringToUrlConverter : BaseConverter<String, URL>(String::class, URL::class) {

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
