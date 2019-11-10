package kt.school.starlord.model.data.mapper.converter.localization

import io.mockk.every
import io.mockk.mockkStatic
import kt.school.starlord.ui.global.entity.wrapper.LocalizedTimePassed
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import org.threeten.bp.Instant

internal class LocalizedTimePassedToLongConverterTest{

    private val converter = LocalizedTimePassedToLongConverter()
    private val now = Instant.now()

    @BeforeEach
    fun setUp() {
        mockkStatic(Instant::class)

        every { Instant.now() } returns now
    }

    @TestFactory
    fun `convert "some time ago" to Date`() = listOf(
        "меньше минуты назад" to now,
        "1 минуту назад" to now.minusMillis(60 * MILLIS_IN_SECOND),
        "22 минуты назад" to now.minusMillis(22 * MILLIS_IN_MINUTE),
        "55 минут назад" to now.minusMillis(55 * MILLIS_IN_MINUTE),
        "89 минут назад" to now.minusMillis(89 * MILLIS_IN_MINUTE),
        "1 час назад" to now.minusMillis(MILLIS_IN_HOUR),
        "22 часа назад" to now.minusMillis(22 * MILLIS_IN_HOUR),
        "55 часов назад" to now.minusMillis(55 * MILLIS_IN_HOUR),
        "89 часов назад" to now.minusMillis(89 * MILLIS_IN_HOUR),
        "1 день назад" to now.minusMillis(MILLIS_IN_DAY),
        "22 дня назад" to now.minusMillis(22 * MILLIS_IN_DAY),
        "55 дней назад" to now.minusMillis(55 * MILLIS_IN_DAY),
        "89 дней назад" to now.minusMillis(89 * MILLIS_IN_DAY),
        "более 4 лет назад" to now.minusMillis(MILLIS_IN_MONTH)
    ).map { (input, expected) ->
        DynamicTest.dynamicTest("when I convert \"$input\" then I get ${expected.toEpochMilli()}") {
            assertEquals(expected.toEpochMilli(), converter.convert(LocalizedTimePassed(input)))
        }
    }

    companion object {
        private const val MILLIS_IN_SECOND = 1000L
        private const val MILLIS_IN_MINUTE = MILLIS_IN_SECOND * 60
        private const val MILLIS_IN_HOUR = MILLIS_IN_MINUTE * 60
        private const val MILLIS_IN_DAY = MILLIS_IN_HOUR * 24
        private const val MILLIS_IN_MONTH = MILLIS_IN_DAY * 30
    }
}