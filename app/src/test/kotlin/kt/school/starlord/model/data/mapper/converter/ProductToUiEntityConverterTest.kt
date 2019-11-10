package kt.school.starlord.model.data.mapper.converter

import android.view.View
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kt.school.starlord.R
import kt.school.starlord.domain.data.mapper.BaseConverter
import kt.school.starlord.domain.data.mapper.Mapper
import kt.school.starlord.domain.entity.product.Price
import kt.school.starlord.domain.entity.product.Product
import kt.school.starlord.domain.entity.product.ProductOwner
import kt.school.starlord.domain.entity.product.ProductType
import kt.school.starlord.ui.global.entity.wrapper.LocalizedMoney
import kt.school.starlord.ui.global.entity.wrapper.LocalizedTimePassed
import kt.school.starlord.ui.products.entity.UiProduct
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.anyString
import org.threeten.bp.Instant
import java.security.acl.Owner

internal class ProductToUiEntityConverterTest {

    private val converter = ProductToUiEntityConverter()

    private val now = Instant.now()
    private val lastUpdate = anyString()
    private val price = anyString()

    private val converters = setOf(
        object : BaseConverter<Long, LocalizedTimePassed>(Long::class, LocalizedTimePassed::class) {
            override fun convert(value: Long) = LocalizedTimePassed(lastUpdate)
        },
        object : BaseConverter<Price, LocalizedMoney>(Price::class, LocalizedMoney::class) {
            override fun convert(value: Price) = LocalizedMoney(price)
        })

    @BeforeEach
    fun setUp() {
        mockkStatic(Instant::class)

        every { Instant.now() } returns now

        startKoin {
            modules(module { single { Mapper(converters) } })
        }
    }

    @TestFactory
    fun convert() = mapOf(
        Product(
            id = 123,
                    title = "title",
                    description = "description",
                    type = ProductType.SERVICE,
                    location = "location",
                    image = "image",
                    owner = ProductOwner("owner", 1L),
                    price = mockk(),
                    lastUpdate = mockk(),
                    localizedTimePassed = mockk(),
                    commentsCount = 123,
                    isPaid = true
        ) to UiProduct(
            id = 123,
            lastUpdate = lastUpdate,
            type = ProductType.SERVICE.name
        ),
        mockProduct().apply {
            every { price } returns ProductPrice(null, ArgumentMatchers.anyBoolean())
        } to "",
        mockProduct().apply {
            every { price } returns ProductPrice(0.0, ArgumentMatchers.anyBoolean())
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
        mockProduct().apply {
            every { price } returns ProductPrice(
                0.0,
                ArgumentMatchers.anyBoolean()
            )
        } to View.VISIBLE,
        mockProduct().apply {
            every { price } returns ProductPrice(
                100.0,
                ArgumentMatchers.anyBoolean()
            )
        } to View.VISIBLE,
        mockProduct().apply { every { price } returns ProductPrice(null, ArgumentMatchers.anyBoolean()) } to View.GONE
    ).map { (input, expected) ->
        DynamicTest.dynamicTest("when I convert \"$input\" then UiProduct.priceVisibility is $expected") {
            val uiProduct = converter.convert(input)
            Assertions.assertEquals(expected, uiProduct.priceVisibility)
        }
    }

    @TestFactory
    fun `convert bargain to visibility`() = mapOf(
        mockProduct().apply {
            every { price } returns ProductPrice(ArgumentMatchers.anyDouble(), false)
        } to View.GONE,
        mockProduct().apply {
            every { price } returns ProductPrice(ArgumentMatchers.anyDouble(), true)
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