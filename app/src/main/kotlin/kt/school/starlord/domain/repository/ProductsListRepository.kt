package kt.school.starlord.domain.repository

import kt.school.starlord.entity.product.ProductsList

/**
 * Defines access methods for products.
 */
interface ProductsListRepository {

    /**
     * Loads data that represents products list by subcategory name and product response by product type
     *
     * @return products
     */
    suspend fun getProducts(subcategory: String): ProductsList
}
