package kt.school.starlord.model.data.mapper.converter

import android.view.View
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kt.school.starlord.R
import kt.school.starlord.domain.entity.product.Product
import kt.school.starlord.domain.entity.product.ProductPrice
import kt.school.starlord.model.data.resources.ResourceManager
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import org.mockito.ArgumentMatchers.anyBoolean
import org.mockito.ArgumentMatchers.anyDouble
import org.mockito.ArgumentMatchers.anyString
import org.threeten.bp.Instant

class ProductToUiEntityConverterTest {

    private val resourceManager: ResourceManager = mockk(relaxed = true)
    private val converter = ProductToUiEntityConverter(resourceManager)
    private val now = Instant.now()
    private val millisNow = now.toEpochMilli()

    @BeforeEach
    fun setUp() {
        mockkStatic(Instant::class)

        every { Instant.now() } returns now
    }

    @TestFactory
    fun `convert price to String`() = mapOf(
        mockProduct().apply {
            every { price } returns ProductPrice(999.0, anyBoolean())
            every { resourceManager.getString(R.string.price, "999") } returns "999р"
        } to "999р",
        mockProduct().apply {
            every { price } returns ProductPrice(null, anyBoolean())
        } to "",
        mockProduct().apply {
            every { price } returns ProductPrice(0.0, anyBoolean())
            every { resourceManager.getString(R.string.for_free) } returns "free"
        } to "free"
    ).map { (input, expected) ->
        DynamicTest.dynamicTest("when I convert \"$input\" then UiProduct.price is $expected") {
            val uiProduct = converter.convert(input)
            Assertions.assertEquals(expected, uiProduct.price)
        }
    }

    @TestFactory
    fun `convert price to visibility`() = mapOf(
        mockProduct().apply { every { price } returns ProductPrice(0.0, anyBoolean()) } to View.VISIBLE,
        mockProduct().apply { every { price } returns ProductPrice(100.0, anyBoolean()) } to View.VISIBLE,
        mockProduct().apply { every { price } returns ProductPrice(null, anyBoolean()) } to View.GONE
    ).map { (input, expected) ->
        DynamicTest.dynamicTest("when I convert \"$input\" then UiProduct.priceVisibility is $expected") {
            val uiProduct = converter.convert(input)
            Assertions.assertEquals(expected, uiProduct.priceVisibility)
        }
    }

    @TestFactory
    fun `convert bargain to visibility`() = mapOf(
        mockProduct().apply {
            every { price } returns ProductPrice(anyDouble(), false)
        } to View.GONE,
        mockProduct().apply {
            every { price } returns ProductPrice(anyDouble(), true)
        } to View.VISIBLE
    ).map { (input, expected) ->
        DynamicTest.dynamicTest("when I convert \"$input\" then UiProduct.bargainVisibility is $expected") {
            val uiProduct = converter.convert(input)
            Assertions.assertEquals(expected, uiProduct.bargainVisibility)
        }
    }

    @TestFactory
    fun `convert last update to String`() = mapOf(
        mockProduct().apply {
            every { lastUpdate } returns millisNow - MILLIS_IN_MONTH
            every { resourceManager.getString(R.string.more_than_month_ago) } returns "more than month ago"
        } to "more than month ago",
        mockProduct().apply {
            every { lastUpdate } returns millisNow - 3 * MILLIS_IN_DAY
            every { resourceManager.getPlural(R.plurals.days_ago, 3, 3L) } returns "3 day ago"
        } to "3 day ago",
        mockProduct().apply {
            every { lastUpdate } returns millisNow - MILLIS_IN_DAY
            every { resourceManager.getPlural(R.plurals.days_ago, 1, 1L) } returns "1 day ago"
        } to "1 day ago",
        mockProduct().apply {
            every { lastUpdate } returns millisNow - 3 * MILLIS_IN_HOUR
            every { resourceManager.getPlural(R.plurals.hours_ago, 3, 3L) } returns "3 hours ago"
        } to "3 hours ago",
        mockProduct().apply {
            every { lastUpdate } returns millisNow - MILLIS_IN_HOUR
            every { resourceManager.getPlural(R.plurals.hours_ago, 1, 1L) } returns "1 hour ago"
        } to "1 hour ago",
        mockProduct().apply {
            every { lastUpdate } returns millisNow - 3 * MILLIS_IN_MINUTE
            every { resourceManager.getPlural(R.plurals.minutes_ago, 3, 3L) } returns "3 min ago"
        } to "3 min ago",
        mockProduct().apply {
            every { lastUpdate } returns millisNow - MILLIS_IN_MINUTE
            every { resourceManager.getPlural(R.plurals.minutes_ago, 1, 1L) } returns "1 min ago"
        } to "1 min ago",
        mockProduct().apply {
            every { lastUpdate } returns millisNow
            every { resourceManager.getString(R.string.less_than_minute_ago) } returns "less than minute ago"
        } to "less than minute ago"
    ).map { (input, expected) ->
        DynamicTest.dynamicTest("when I convert \"$input\" then UiProduct.lastUpdate is $expected") {
            val uiProduct = converter.convert(input)
            Assertions.assertEquals(expected, uiProduct.lastUpdate)
        }
    }

    @TestFactory
    fun `convert comments count to visibility`() = mapOf(
        mockProduct().apply { every { commentsCount } returns 0 } to View.GONE,
        mockProduct().apply { every { commentsCount } returns 1 } to View.VISIBLE
    ).map { (input, expected) ->
        DynamicTest.dynamicTest("when I convert \"$input\" then UiProduct.commentsCountVisibility is $expected") {
            val uiProduct = converter.convert(input)
            Assertions.assertEquals(expected, uiProduct.commentsCountVisibility)
        }
    }

    private fun mockProduct() = mockk<Product>(relaxed = true).apply {
        every { title } returns anyString()
        every { description } returns anyString()
        every { type } returns mockk(relaxed = true)
        every { location } returns anyString()
        every { image } returns anyString()
        every { owner } returns mockk(relaxed = true)
        every { price } returns mockk(relaxed = true)
    }

    companion object {
        private const val MILLIS_IN_SEC = 1000L
        private const val MILLIS_IN_MINUTE = 60 * MILLIS_IN_SEC
        private const val MILLIS_IN_HOUR = 60 * MILLIS_IN_MINUTE
        private const val MILLIS_IN_DAY = 24 * MILLIS_IN_HOUR
        private const val MILLIS_IN_MONTH = 30 * MILLIS_IN_DAY
    }
}
