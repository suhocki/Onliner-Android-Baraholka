package kt.school.starlord.domain.repository.product

import androidx.paging.DataSource
import kt.school.starlord.domain.entity.product.Product

/**
 * Defines access methods for products that is located in cache.
 */
interface ProductsCacheRepository {

    /**
     * Loads products live data from cache.
     */
    fun getProductsCached(subcategoryName: String): DataSource.Factory<Int, Product>

    /**
     * Deletes existing products of selected subcategoryName and puts a new ones.
     */
    suspend fun updateProducts(subcategoryName: String, products: List<Product>)
}
