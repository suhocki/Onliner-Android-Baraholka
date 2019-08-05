package kt.school.starlord.model.data.mapper.converter

import kt.school.starlord.BuildConfig
import org.junit.Test

class StringToUrlConverterTest {
    @Test
    fun `convert String to Url starts with dot-slash`() {
        // Given
        val converter = StringToUrlConverter()

        // When
        val result = converter.convert("./")

        // Then
        assert("$result" == "${BuildConfig.BARAHOLKA_ONLINER_URL}/")
    }

    @Test
    fun `convert String to Url starts with https`() {
        // Given
        val converter = StringToUrlConverter()
        val string = "https://play.google.com/music/listen?authuser&u=0"

        // When
        val result = converter.convert(string)

        // Then
        assert("$result" == string)
    }
}
