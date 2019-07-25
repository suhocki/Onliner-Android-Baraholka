package kt.school.starlord.model.data.mapper.converter

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.jsoup.Jsoup
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.AutoCloseKoinTest

@RunWith(AndroidJUnit4::class)
class DocumentToCategoriesWithSubcategoriesConverterTest : AutoCloseKoinTest() {

    private val converter = DocumentToCategoriesWithSubcategoriesConverter()

    @Test
    fun `convert document to categories with subcategories`() {
        // Given
        val assets = InstrumentationRegistry.getInstrumentation().context.assets
        val inputStream = assets.open("html/categories_with_subcategories.html")
        val document = Jsoup.parse(inputStream, "UTF-8", "")

        // When
        val categoriesWithSubcategories = converter.convert(document)

        // Then
        with(categoriesWithSubcategories) {
            assert(categories.size == 25)
            assert(subcategories.size == 228)
        }
    }
}
