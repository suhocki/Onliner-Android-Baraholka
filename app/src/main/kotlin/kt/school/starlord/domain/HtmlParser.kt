package kt.school.starlord.domain

import kt.school.starlord.domain.entity.category.Category
import kt.school.starlord.domain.entity.product.Product
import kt.school.starlord.domain.entity.subcategory.Subcategory

interface HtmlParser {
    suspend fun parseCategories(html: String): Map<Category, List<Subcategory>>
    suspend fun parseProducts(html: String): List<Product>
}