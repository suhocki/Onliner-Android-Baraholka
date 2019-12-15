package kt.school.starlord.model.data.mapper.converter.localization

import io.mockk.every
import io.mockk.mockk
import kt.school.starlord.R
import kt.school.starlord.model.data.android.ResourceManager
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory

internal class LongToLocalizedTimePassedConverterTest {

    private val resourceManager: ResourceManager = mockk()
    private val converter = LongToLocalizedTimePassedConverter(resourceManager)

    @BeforeEach
    fun setUp() {
        every { resourceManager.getString(R.string.less_than_minute_ago) } returns "less_than_minute_ago"
        every { resourceManager.getPlural(R.plurals.minutes_ago, 1, 1L) } returns "1 min ago"
        every { resourceManager.getPlural(R.plurals.hours_ago, 1, 1L) } returns "1 hour ago"
        every { resourceManager.getPlural(R.plurals.days_ago, 1, 1L) } returns "1 day ago"
        every { resourceManager.getPlural(R.plurals.month_ago, 1, 1L) } returns "1 month ago"
        every { resourceManager.getPlural(R.plurals.month_ago, 2, 2L) } returns "2 month ago"
    }

    @TestFactory
    fun convertElements() = mapOf(
        MILLIS_IN_SEC to "less_than_minute_ago",
        MILLIS_IN_MINUTE to "1 min ago",
        MILLIS_IN_HOUR to "1 hour ago",
        MILLIS_IN_DAY to "1 day ago",
        MILLIS_IN_MONTH to "1 month ago",
        MILLIS_IN_MONTH * 2 to "2 month ago"
    ).map { (input, expected) ->
        DynamicTest.dynamicTest("Converting $input to localized time passed") {
            val actual = converter.convert(input)
            Assertions.assertEquals(expected, actual.value)
        }
    }

    companion object {
        private const val MILLIS_IN_SEC = 1000L
        private const val MILLIS_IN_MINUTE = 60 * MILLIS_IN_SEC
        private const val MILLIS_IN_HOUR = 60 * MILLIS_IN_MINUTE
        private const val MILLIS_IN_DAY = 24 * MILLIS_IN_HOUR
        private const val MILLIS_IN_MONTH = 30 * MILLIS_IN_DAY
    }
}
