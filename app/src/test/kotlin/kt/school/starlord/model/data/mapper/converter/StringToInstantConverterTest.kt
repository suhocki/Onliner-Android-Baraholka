package kt.school.starlord.model.data.mapper.converter

import io.mockk.every
import io.mockk.mockkStatic
import kt.school.starlord.domain.entity.global.TimeUnit
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import org.threeten.bp.Instant

class StringToInstantConverterTest {

    private val converter = StringToTimestamp()
    private val now = Instant.now()

    @BeforeEach
    fun setUp() {
        mockkStatic(Instant::class)

        every { Instant.now() } returns now
    }

    @TestFactory
    fun `convert "some time ago" to Date`() = listOf(
        "меньше минуты назад" to now,
        "1 минуту назад" to now.minusMillis(60 * TimeUnit.SECOND.millis),
        "22 минуты назад" to now.minusMillis(22 * TimeUnit.MINUTE.millis),
        "55 минут назад" to now.minusMillis(55 * TimeUnit.MINUTE.millis),
        "89 минут назад" to now.minusMillis(89 * TimeUnit.MINUTE.millis),
        "1 час назад" to now.minusMillis(TimeUnit.HOUR.millis),
        "22 часа назад" to now.minusMillis(22 * TimeUnit.HOUR.millis),
        "55 часов назад" to now.minusMillis(55 * TimeUnit.HOUR.millis),
        "89 часов назад" to now.minusMillis(89 * TimeUnit.HOUR.millis),
        "1 день назад" to now.minusMillis(TimeUnit.DAY.millis),
        "22 дня назад" to now.minusMillis(22 * TimeUnit.DAY.millis),
        "55 дней назад" to now.minusMillis(55 * TimeUnit.DAY.millis),
        "89 дней назад" to now.minusMillis(89 * TimeUnit.DAY.millis),
        "более 4 лет назад" to Instant.ofEpochMilli(0)
    ).map { (input, expected) ->
        DynamicTest.dynamicTest("when I convert \"$input\" then I get ${expected.epochSecond}") {
            Assertions.assertEquals(expected.epochSecond, converter.convert(input).epochSecond)
        }
    }
}
