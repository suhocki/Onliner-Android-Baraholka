package kt.school.starlord.domain.repository

import kt.school.starlord.entity.product.Product

/**
 * Defines access methods for products.
 */
interface ProductsListRepository {

    /**
     * Loads data that represents products list by subcategory link.
     *
     * @return products
     */
    suspend fun getProducts(link: String): List<Product>
}
