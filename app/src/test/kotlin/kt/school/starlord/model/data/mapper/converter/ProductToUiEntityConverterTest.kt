package kt.school.starlord.model.data.mapper.converter

import android.view.View
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kt.school.starlord.domain.data.mapper.BaseConverter
import kt.school.starlord.domain.data.mapper.Mapper
import kt.school.starlord.domain.entity.product.Price
import kt.school.starlord.domain.entity.product.Product
import kt.school.starlord.domain.entity.product.ProductOwner
import kt.school.starlord.domain.entity.product.ProductType
import kt.school.starlord.ui.global.entity.wrapper.LocalizedMoney
import kt.school.starlord.ui.global.entity.wrapper.LocalizedTimePassed
import kt.school.starlord.ui.products.entity.UiProduct
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.ArgumentMatchers.anyString
import org.threeten.bp.Instant

internal class ProductToUiEntityConverterTest : KoinTest {

    private val converter by lazy { ProductToUiEntityConverter() }

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

    @AfterEach
    fun afterEach() {
        stopKoin()
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
            price = Price(amount = 123.0, hasPrice = true, isBargainAvailable = true),
            lastUpdate = anyLong(),
            localizedTimePassed = mockk(),
            commentsCount = 12,
            isPaid = true
        ) to UiProduct(
            id = 123,
            lastUpdate = lastUpdate,
            type = ProductType.SERVICE.stringRes,
            typeColor = ProductType.SERVICE.colorRes,
            location = "location",
            image = "image",
            owner = "owner",
            title = "title",
            description = "description",
            comments = "12",
            price = price,
            isPaid = true,
            bargainVisibility = View.VISIBLE,
            commentsCountVisibility = View.VISIBLE,
            priceVisibility = View.VISIBLE
        ), Product(
            id = 123,
            title = "title",
            description = "description",
            type = ProductType.BUY,
            location = "location",
            image = "image",
            owner = ProductOwner("owner", 1L),
            price = Price(amount = 123.0, hasPrice = false, isBargainAvailable = false),
            lastUpdate = anyLong(),
            localizedTimePassed = mockk(),
            commentsCount = 0,
            isPaid = true
        ) to UiProduct(
            id = 123,
            lastUpdate = lastUpdate,
            type = ProductType.BUY.stringRes,
            typeColor = ProductType.BUY.colorRes,
            location = "location",
            image = "image",
            owner = "owner",
            title = "title",
            description = "description",
            comments = "0",
            price = price,
            isPaid = true,
            bargainVisibility = View.GONE,
            commentsCountVisibility = View.INVISIBLE,
            priceVisibility = View.GONE
        )
    ).map { (input, expected) ->
        DynamicTest.dynamicTest("Converting domain product to UI") {
            val uiProduct = converter.convert(input)
            Assertions.assertEquals(expected, uiProduct)
        }
    }
}
