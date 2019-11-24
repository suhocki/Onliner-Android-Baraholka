package kt.school.starlord.model.repository.network

import kt.school.starlord.domain.data.mapper.Mapper
import kt.school.starlord.domain.entity.category.Category
import kt.school.starlord.domain.entity.product.Product
import kt.school.starlord.domain.entity.subcategory.Subcategory
import kt.school.starlord.domain.repository.CategoriesWithSubcategoriesRepository
import kt.school.starlord.domain.repository.product.ProductsRepository
import kt.school.starlord.model.data.jsoup.JsoupDataSource

/**
 * Fetch data through the Internet connection.
 */
class NetworkRepository(
    private val jsoupDataSource: JsoupDataSource,
    private val mapper: Mapper
) : CategoriesWithSubcategoriesRepository, ProductsRepository {

    override suspend fun getCategoriesWithSubcategories() =
        jsoupDataSource.getCategoriesElements()
            .map { (categoryElement, subcategoryElements) ->
                val category: Category = mapper.map(categoryElement)
                val subcategories = subcategoryElements
                    .map { mapper.map<Subcategory>(it) }
                    .apply { forEach { subcategory -> subcategory.categoryName = category.name } }

                category to subcategories
            }
            .toMap()

    override suspend fun getProducts(link: String) =
        jsoupDataSource.getProductElements(mapper.map(link))
            .map { mapper.map<Product>(it) }
}
