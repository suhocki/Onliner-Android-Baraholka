package kt.school.starlord.domain.repository.product

import kt.school.starlord.entity.product.Product

/**
 * Defines access methods for products located on the remote server.
 */
interface ProductsRepository {

    /**
     * Loads data that represents products list by subcategory link.
     *
     * @return products
     */
    suspend fun getProducts(link: String): List<Product>
}
