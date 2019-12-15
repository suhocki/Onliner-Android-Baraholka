package kt.school.starlord.domain

import kt.school.starlord.domain.entity.category.Category
import kt.school.starlord.domain.entity.product.Product
import kt.school.starlord.domain.entity.subcategory.Subcategory

/**
 * Parses HTML to in-app entities.
 */
interface HtmlParser {
    /**
     * html to categories.
     */
    suspend fun parseCategories(html: String): Map<Category, List<Subcategory>>
    /**
     * html to products.
     */
    suspend fun parseProducts(html: String): List<Product>
}
