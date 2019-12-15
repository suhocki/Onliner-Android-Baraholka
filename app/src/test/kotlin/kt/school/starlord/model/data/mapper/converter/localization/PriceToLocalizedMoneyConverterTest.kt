package kt.school.starlord.model.data.mapper.converter.localization

import io.mockk.every
import io.mockk.mockk
import kt.school.starlord.R
import kt.school.starlord.domain.entity.product.Price
import kt.school.starlord.model.data.android.ResourceManager
import kt.school.starlord.ui.global.entity.wrapper.LocalizedMoney
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory

internal class PriceToLocalizedMoneyConverterTest {

    private val resourceManager: ResourceManager = mockk()
    private val converter = PriceToLocalizedMoneyConverter(resourceManager)

    @BeforeEach
    fun setUp() {
        every { resourceManager.getString(R.string.for_free) } returns "free"
        every { resourceManager.getString(R.string.price, "1,1") } returns "1,1 r."
    }

    @TestFactory
    fun convertElements() = mapOf(
        Price(amount = 1.1, hasPrice = true, isBargainAvailable = true) to LocalizedMoney("1,1 r."),
        Price(amount = 0.0, hasPrice = true, isBargainAvailable = true) to LocalizedMoney("free")
    ).map { (input, expected) ->
        DynamicTest.dynamicTest("Price ${input.amount} -> ${expected.value}") {
            val actual = converter.convert(input)
            Assertions.assertEquals(expected, actual)
        }
    }
}
