package kt.school.starlord.model.data.mapper.converter

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.jsoup.Jsoup
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.AutoCloseKoinTest

@RunWith(AndroidJUnit4::class)
class DocumentToProductConverterTest : AutoCloseKoinTest() {

    private val converter = DocumentToProductsListConverter()

    @Test
    fun `convert document to products`() {
        // Given
        val assets = InstrumentationRegistry.getInstrumentation().context.assets
        val inputStream = assets.open("html/products.html")
        val document = Jsoup.parse(inputStream, "UTF-8", "")

        // When
        val productsList = converter.convert(document)

        // Then
        with(productsList) {
            assert(products.size == 62)
        }
    }
}
