package kt.school.starlord.model.data.jsoup

import kotlinx.coroutines.withContext
import kt.school.starlord.domain.HtmlParser
import kt.school.starlord.domain.entity.category.Category
import kt.school.starlord.domain.entity.product.Product
import kt.school.starlord.domain.entity.subcategory.Subcategory
import kt.school.starlord.domain.mapper.Mapper
import kt.school.starlord.domain.system.coroutine.CoroutineContextProvider
import org.jsoup.Jsoup

class JsoupParser(contexts: CoroutineContextProvider, private val mapper: Mapper) : HtmlParser {

    private val context = contexts.computation

    override suspend fun parseCategories(html: String) = withContext(context) {
        Jsoup.parse(html)
            .getElementsByClass(CATEGORIES)
            .associateWith {
                it.getElementsByClass(SUBCATEGORIES)
                    .first()
                    .select(SUBCATEGORIES_DATA)
            }
            .map { (categoryElement, subcategoryElements) ->
                val category: Category = mapper.map(categoryElement)
                val subcategories = subcategoryElements
                    .map { mapper.map<Subcategory>(it) }
                    .apply { forEach { subcategory -> subcategory.categoryName = category.name } }

                category to subcategories
            }
            .toMap()
    }

    override suspend fun parseProducts(html: String) = withContext(context) {
        Jsoup.parse(html)
            .getElementsByClass(PRODUCT_CLASS)
            .flatMap { it.getElementsByTag(PRODUCT_TAG) }
            .filter {
                it.getElementsByClass(PRODUCT_TITLE).hasText() &&
                        it.getElementsByClass(PRODUCT_SIGNATURE).first() != null &&
                        it.getElementsByClass(PRODUCT_COST).first() != null
            }
            .map { mapper.map<Product>(it) }
    }

    companion object {
        private const val CATEGORIES = "cm-onecat"
        private const val PRODUCT_CLASS = "ba-tbl-list__table"
        private const val PRODUCT_TAG = "tr"
        private const val PRODUCT_TITLE = "wraptxt"
        private const val PRODUCT_SIGNATURE = "ba-signature"
        private const val PRODUCT_COST = "cost"
        private const val SUBCATEGORIES = "b-cm-list"
        private const val SUBCATEGORIES_DATA = "li"
    }
}